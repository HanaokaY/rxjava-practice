package rxjavapractice;

import io.reactivex.rxjava3.core.Observable;

public class App {
    public static void main(String[] args) {
        Observable<String> observable = Observable.create(emitter -> {
            emitter.onNext("Hello");
            emitter.onNext("World");
            emitter.onComplete();
        });

        observable.subscribe(
                item -> System.out.println(item), // onNext
                throwable -> System.err.println("Error occurred!"), // onError
                () -> System.out.println("Done!") // onComplete
        );
    }
}
