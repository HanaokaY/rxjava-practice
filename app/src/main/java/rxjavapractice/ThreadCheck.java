package rxjavapractice;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ThreadCheck {
    public static void main(String[] args) throws InterruptedException {
        Observable<String> observable = Observable.create(emitter -> {
            System.out.println(" エミットしたスレッド =>  " + Thread.currentThread().getName());
            emitter.onNext("Hello");
            emitter.onNext("World");
            emitter.onComplete();
        });

        observable
                .observeOn(Schedulers.io()) // これをコメントアウトすると、下記処理はmainスレッドで行われる。
                .subscribe(
                        item -> {
                            System.out.println("受け取ったデータ => " + item + ": スレッド => " + Thread.currentThread().getName());
                        },
                        throwable -> {
                            System.err.println("Error  スレッド => " + Thread.currentThread().getName());
                        },
                        () -> {
                            System.out.println("完了したスレッド => " + Thread.currentThread().getName());
                        }
                );

        Thread.sleep(1000);
    }
}
