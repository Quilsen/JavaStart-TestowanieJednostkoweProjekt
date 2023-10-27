package pl.javastart.dnd;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {
    @Mock AnimalRepository animalRepository;
    @InjectMocks AnimalService animalService;
    @Captor ArgumentCaptor<Animal> animalCaptor;
    @Captor
    ArgumentCaptor<List<Animal>> animalListCaptor;
    @Test
    void shouldCalculateSortOrder_WhenMovedToTheStart(){
        //given
        Animal kon = createAnimal("kon", 100L);
        Animal pies = createAnimal("pies", 200L);
        Animal krowa = createAnimal("krowa", 300L);
        List<Animal> animalList = createList(kon, pies, krowa);

        //when
        long calculatedSortOrder = animalService.calculateSortOrder(0, animalList);

        //then
        assertThat(calculatedSortOrder).isEqualTo(0L);
    }

    @Test
    void shouldCalculateSortOrder_WhenMovedToTheEnd(){
        //given
        Animal kon = createAnimal("kon", 100L);
        Animal pies = createAnimal("pies", 200L);
        Animal krowa = createAnimal("krowa", 300L);
        List<Animal> animalList = createList(kon, pies, krowa);

        //when
        long calculatedSortOrder = animalService.calculateSortOrder(2, animalList);

        //then
        assertThat(calculatedSortOrder).isEqualTo(400L);
    }

    @Test
    void shouldCalculateSortOrder_WhenMovedToTheMiddle(){
        //given
        Animal kon = createAnimal("kon", 100L);
        Animal pies = createAnimal("pies", 200L);
        Animal krowa = createAnimal("krowa", 300L);
        List<Animal> animalList = createList(kon, pies, krowa);

        //when
        long calculatedSortOrder = animalService.calculateSortOrder(1, animalList);

        //then
        assertThat(calculatedSortOrder).isEqualTo(150L);
    }

    @Test
    void shouldThrowCanNotCalculateSetSortOrderException(){
        //given
        Animal kon = createAnimal("kon", 100L);
        Animal pies = createAnimal("pies", 101L);
        Animal krowa = createAnimal("krowa", 103L);

        List<Animal> animalList = createList(kon, pies, krowa);

        //when
        //then
        assertThatThrownBy(() -> animalService.calculateSortOrder(1, animalList))
                .isInstanceOf(CanNotCalculateSetSortOrderException.class);
    }

    @Test
    void shouldResetSortOrder(){
        //given
        Animal kon = createAnimal("kon", 100L);
        Animal pies = createAnimal("pies", 101L);
        Animal krowa = createAnimal("krowa", 103L);
        List<Animal> animalList = createList(kon, pies, krowa);

        //when
        animalService.resetSortOrder(1,krowa,animalList);

        //then
        verify(animalRepository).saveAll(animalListCaptor.capture());

        List<Animal> animalListCaptorValue = animalListCaptor.getValue();
        assertThat(animalListCaptorValue.get(0).getName()).isEqualTo("kon");
        assertThat(animalListCaptorValue.get(0).getSortOrder()).isEqualTo(100L);
        assertThat(animalListCaptorValue.get(1).getName()).isEqualTo("krowa");
        assertThat(animalListCaptorValue.get(1).getSortOrder()).isEqualTo(200L);
        assertThat(animalListCaptorValue.get(2).getName()).isEqualTo("pies");
        assertThat(animalListCaptorValue.get(2).getSortOrder()).isEqualTo(300L);
    }

    @Test
    void shouldSaveCorrectAnimalToAnimalRepository(){
        //given
        long animalId = 3L;
        int targetPosition = 1;
        Animal kon = createAnimalWithId(1L,"kon", 100L);
        Animal pies = createAnimalWithId(2L,"pies", 200L);
        Animal krowa = createAnimalWithId(3L,"krowa", 300L);
        List<Animal> animalList = createList(kon, pies, krowa);

        when(animalRepository.findById(animalId)).thenReturn(Optional.of(krowa));
        when(animalService.findAllSorted()).thenReturn(animalList);

        //when
        animalService.move(animalId,targetPosition);

        //then
        verify(animalRepository).save(animalCaptor.capture());

        Animal animalCaptorValue = animalCaptor.getValue();
        assertThat(animalCaptorValue).isNotNull();
        assertThat(animalCaptorValue.getId()).isEqualTo(3L);
        assertThat(animalCaptorValue.getName()).isEqualTo("krowa");
        assertThat(animalCaptorValue.getSortOrder()).isEqualTo(150L);
    }


    private List<Animal> createList(Animal... animals) {
        return new LinkedList<>(Arrays.asList(animals));
    }

    private Animal createAnimal(String name, long sortOrder) {
        Animal animal = new Animal();
        animal.setName(name);
        animal.setSortOrder(sortOrder);
        return animal;
    }
    private Animal createAnimalWithId(Long Id,String name, long sortOrder) {
        Animal animal = new Animal();
        animal.setId(Id);
        animal.setName(name);
        animal.setSortOrder(sortOrder);
        return animal;
    }
}