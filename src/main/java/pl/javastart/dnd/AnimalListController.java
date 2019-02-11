package pl.javastart.dnd;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/animals")
@RestController
public class AnimalListController {

    private AnimalService animalService;

    public AnimalListController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @GetMapping("")
    public List<Animal> getAll() {
        return animalService.findAllSorted();
    }

    @PostMapping("/{id}/position")
    public void move(@PathVariable Long id, @RequestBody Map<String, String> json) {
        int position = Integer.valueOf(json.get("position")) ;
        animalService.move(id, position);
    }

}
