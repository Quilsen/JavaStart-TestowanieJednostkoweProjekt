package pl.javastart.dnd;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    List<Animal> findAll() {
        return animalRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Animal::getSortOrder))
                .collect(Collectors.toList());
    }

    void move(Long animalId, int targetPosition) {
        // TODO uzupełnij tę metodę oraz dodaj do niej testy, żeby sprawdzić czy działa

        // w ten sposób możesz pobrać wszystkie zwierzaki z bazy danych
        List<Animal> allAnimals = animalRepository.findAll();

        // w ten sposób pobrac jednego zwierzaka po ID
        Animal animal = animalRepository.findById(animalId).orElseThrow();

        // w ten sposób zapisać zmiany dotyczące zwierzaka do bazy
        animal.setSortOrder(1000L);
        animalRepository.save(animal);

        // w ten sposób zapisać zmiany w całej liście zwierzaków na raz
        animalRepository.saveAll(allAnimals);

        // POWODZENIA!
    }
}
