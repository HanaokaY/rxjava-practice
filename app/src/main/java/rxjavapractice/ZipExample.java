package rxjavapractice;
import io.reactivex.rxjava3.core.Observable;

public class ZipExample {
    public static void main(String[] args) {
        Observable<String> observable1 = Observable.just("Apple", "Banana", "Cherry");
        Observable<Integer> observable2 = Observable.just(1, 2, 3);

        Observable.zip(observable1, observable2, (fruit, number) -> "Fruit " + number + " is " + fruit)
                .subscribe(result -> System.out.println(result));
    }
}
