package com.thehecklers.sburrestdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class SburRestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SburRestDemoApplication.class, args);
    }
}

    @RestController
    @RequestMapping("/coffees")
    class RestApiController {
        // 처리할 데이터
        private List<Coffee> coffees = new ArrayList<>();

        // 생성자
        public RestApiController() {
            coffees.addAll(List.of(
                new Coffee("Cafe Cereze"),
                new Coffee("Cafe Ganador"),
                new Coffee("Cafe Lareno"),
                new Coffee("Cafe Tres Pontas")
            ));
        }

        /*
        각 메소드별로 동작하는 맵핑 어노테이션
        GetMapping, PostMapping, PutMapping, DeleteMapping
         */
        // GET 방식으로 요청 처리
//        @RequestMapping(value = "/coffees", method = RequestMethod.GET)
        @GetMapping
        Iterable<Coffee> getCoffees() {
            return coffees;
        }
        // 특정 ID를 통해 불러오기
        @GetMapping("/{id}")
        Optional<Coffee> getCoffee(@PathVariable String id) {
            for (Coffee coffee : coffees) {
                if (coffee.getId().equals(id)) {
                    return Optional.of(coffee);
                }
            }
            return Optional.empty();
        }
        // POST - 생성
        @PostMapping
        Coffee postCoffee(@RequestBody Coffee coffee) {
            coffees.add(coffee);
            return coffee;
        }
        // PUT
        @PutMapping("/{id}")
        ResponseEntity<Coffee> putCoffee(@PathVariable String id, @RequestBody Coffee coffee) {
            int coffeeIndex = -1;

            for (Coffee c : coffees) {
                if (c.getId().equals(id)) {
                    coffeeIndex = coffees.indexOf(c);
                    coffees.set(coffeeIndex, coffee);
                }
            }
            return (coffeeIndex == -1) ? new ResponseEntity<>(postCoffee(coffee),HttpStatus.CREATED) : new ResponseEntity<>(coffee,HttpStatus.OK);
        }
        // DELETE
        @DeleteMapping("/{id}")
        void deleteCoffee(@PathVariable String id) {
            coffees.removeIf(c -> c.getId().equals(id));
        }
    }


    // 도메인 생성(coffee)
    class Coffee {
        // 필드
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
