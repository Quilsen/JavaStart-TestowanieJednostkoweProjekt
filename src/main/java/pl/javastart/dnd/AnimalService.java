package pl.javastart.dnd;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    private static final long SORT_ORDER_STEP = 100;
    private AnimalRepository animalRepository;

    public AnimalService(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    List<Animal> findAllSorted() {
        return animalRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Animal::getSortOrder))
                .collect(Collectors.toList());
    }

    void move(Long animalId, int targetPosition) {
        // TODO uzupełnij tę metodę oraz dodaj do niej testy, żeby sprawdzić czy działa
        Animal animal = animalRepository.findById(animalId).orElseThrow();
        List<Animal> animalsSorted = findAllSorted();

        try {
            long sortOrderToSet = calculateSortOrder(targetPosition, animalsSorted);
            animal.setSortOrder(sortOrderToSet);
            animalRepository.save(animal);
        } catch (CanNotCalculateSetSortOrderException e){
            System.out.println(e.getMessage());
            resetSortOrder(targetPosition, animal, animalsSorted);
        }
    }

     long calculateSortOrder(int targetPosition, List<Animal> animalsSorted){
        long sortOrderToSet;
        if (targetPosition == 0) {
            Animal animalFirst = animalsSorted.get(targetPosition);
            sortOrderToSet = animalFirst.getSortOrder() - SORT_ORDER_STEP;
        } else if (targetPosition == animalsSorted.size() - 1) {
            Animal animalLast = animalsSorted.get(targetPosition);
            sortOrderToSet = animalLast.getSortOrder() + SORT_ORDER_STEP;
        } else {
            Animal animalBefore = animalsSorted.get(targetPosition - 1);
            Animal animalAfter = animalsSorted.get(targetPosition);
            sortOrderToSet = (animalBefore.getSortOrder() + animalAfter.getSortOrder()) / 2;
            if (animalAfter.getSortOrder() == sortOrderToSet || animalBefore.getSortOrder() == sortOrderToSet) {
                throw new CanNotCalculateSetSortOrderException();
            }
        }
        return sortOrderToSet;
    }

    void resetSortOrder(int targetPosition, Animal animal, List<Animal> animalsSorted){
        animalsSorted.remove(animal);
        animalsSorted.add(targetPosition,animal);
        for (int i = 0; i < animalsSorted.size(); i++) {
            animalsSorted.get(i).setSortOrder((i + 1) * SORT_ORDER_STEP);
        }
        animalRepository.saveAll(animalsSorted);
    }
}
