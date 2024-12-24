package com.thehecklers.sburrestdemo;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SburRestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SburRestDemoApplication.class, args);
    }

    // Droid 객체 빈 등록
    @Bean
    @ConfigurationProperties(prefix = "droid")
    public Droid droid() {
        return new Droid();
    }


}

// 초기 데이터를 생성하는 컴포넌트 구현
@Component
class DataLoader {
    private final CoffeeRepository coffeeRepository;
    public DataLoader(CoffeeRepository coffeeRepository) {
        this.coffeeRepository = coffeeRepository;
    }

    @PostConstruct
    private void loadData() {
        this.coffeeRepository.saveAll(List.of(
            new Coffee("Cafe Cereze"),
            new Coffee("Cafe Ganador"),
            new Coffee("Cafe Lareno"),
            new Coffee("Cafe Tres Pontas")
        ));
    }

}
    @RestController
    @RequestMapping("/coffees")
    class RestApiController {
    // ch4 3. 저장소 사용하기
        // 처리할 데이터
//        private List<Coffee> coffees = new ArrayList<>();
// 저장소 호출(spring의 생성자 주입)
        private final CoffeeRepository coffeeRepository;

        // 생성자(수정)
        public RestApiController(CoffeeRepository coffeeRepository) {
            this.coffeeRepository = coffeeRepository;
        }

        /*
        각 메소드별로 동작하는 맵핑 어노테이션
        GetMapping, PostMapping, PutMapping, DeleteMapping
         */
        // GET 방식으로 요청 처리
//        @RequestMapping(value = "/coffees", method = RequestMethod.GET)
        @GetMapping
        Iterable<Coffee> getCoffees() {
            return coffeeRepository.findAll();
        }
        // 특정 ID를 통해 불러오기
        @GetMapping("/{id}")
        Optional<Coffee> getCoffee(@PathVariable String id) {
            return coffeeRepository.findById(id);
        }
        // POST - 생성
        @PostMapping
        Coffee postCoffee(@RequestBody Coffee coffee) {
            return coffeeRepository.save(coffee);
        }
        // PUT
        @PutMapping("/{id}")
        ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
            // 1. id를 이용해 객체가 있는지 확인
            // 2. 있으면 전달 받은 coffee 객체를 이용해 수정
            // 3. 없으면 전달 받은 coffee 객체를 이용해 새롭게 생성

            return (coffeeRepository.existsById(id))
                    ? new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.OK)
                    : new ResponseEntity<>(coffeeRepository.save(coffee), HttpStatus.CREATED);
        }
        // DELETE
        @DeleteMapping("/{id}")
        String deleteCoffee(@PathVariable String id) {
            coffeeRepository.deleteById(id);
            return coffeeRepository.existsById(id) ? "삭제 실패" : "삭제 성공";
        }
    }
// coffee 엔티티에 대한 저장소(repository)
interface CoffeeRepository extends CrudRepository<Coffee, String> {

}

    // 도메인 생성(coffee)
@Entity(name = "coffes")
    class Coffee {
        // 필드
        @Id
        private String id;
        private String name;

        // 생성자
        public Coffee(){}
        public Coffee(String id, String name) {
            this.id = id;
            this.name = name;
        }
        public Coffee(String name) {
            this(UUID.randomUUID().toString(), name);
        }
        // Getter, Setter
        public String getId() {
            return id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setId(String id) {
            this.id = id;
        }


    }
