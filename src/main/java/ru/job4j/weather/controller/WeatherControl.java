package ru.job4j.weather.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.job4j.weather.model.Weather;
import ru.job4j.weather.service.WeatherService;

import java.time.Duration;

@RestController
public class WeatherControl {

    @Autowired
    private final WeatherService weatherService;

    public WeatherControl(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(value = "/all", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> all() {
        Flux<Weather> data = weatherService.all();
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(data, delay).map(Tuple2::getT1);
    }

    @GetMapping(value = "/get/{id}")
    public Mono<Weather> get(@PathVariable Integer id) {
        return weatherService.findById(id);
    }

    @GetMapping(value = "/hottest")
    public Mono<Weather> getHottest() {
        return weatherService.findByHottest();
    }

    @GetMapping(
            value = "/cityGreat/Then/{temperature}",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Weather> allGreatThen(@PathVariable Integer temperature) {
        Flux<Weather> buff = weatherService.allGreatThen(temperature);
        Flux<Long> delay = Flux.interval(Duration.ofSeconds(3));
        return Flux.zip(buff, delay).map(Tuple2::getT1);
    }

}
