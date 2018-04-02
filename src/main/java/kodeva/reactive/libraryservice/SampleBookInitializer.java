package kodeva.reactive.libraryservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Slf4j
@Component
class SampleBookInitializer implements ApplicationRunner {
    private final BookRepository bookRepository;

    SampleBookInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        this.bookRepository
                .deleteAll()
                .thenMany(
                        Flux.just(
                                "Cloud Native Java|jlong",
                                "Spring Security 3.1|rwinch",
                                "Spring in Action|cwalls"))
                .map(t -> t.split("\\|"))
                .map(tuple -> new Book(null, tuple[0], tuple[1]))
                .flatMap(this.bookRepository::save)
                .thenMany(this.bookRepository.findAll())
                .subscribe(book -> log.info(book.toString()));
    }
}
