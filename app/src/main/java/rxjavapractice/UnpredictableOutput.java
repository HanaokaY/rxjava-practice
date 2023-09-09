package rxjavapractice;

import io.reactivex.rxjava3.core.Observable;

public class UnpredictableOutput {
    
    public static void main(String[] args) {
        UnpredictableOutput sample = new UnpredictableOutput();
        sample.runSample();
    }

    public void runSample() {
        Observable<String> unpredictableObservable = Observable.create(emitter -> {
            try {
                System.out.println("Data fetch started");
                
                // 意図的な遅延を設けて、外部の影響を模擬します
                Thread.sleep(1000);
                
                // 突如としてデータが変更される場合を模擬します
                if(Math.random() > 0.5) {
                    emitter.onNext("Expected Data");
                } else {
                    emitter.onNext("Unexpected Data");
                }
                
                emitter.onComplete();
            } catch (InterruptedException e) {
                emitter.onError(e);
            }
        });
        
        unpredictableObservable
                .subscribe(
                        data -> System.out.println("Received: " + data), 
                        throwable -> System.err.println("Error: " + throwable.getMessage()), 
                        () -> System.out.println("Complete")
                );
    }
}
