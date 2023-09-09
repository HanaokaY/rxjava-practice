package rxjavapractice;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.FlowableEmitter;
import io.reactivex.rxjava3.core.FlowableOnSubscribe;
import io.reactivex.rxjava3.schedulers.Schedulers;

/** Flowableで実装した場合のサンプル */
public class FlowableSampleDemo {
  
  public static void main(String[] args) throws Exception {
    // あいさつの言葉を通知するFlowableの生成
    Flowable<String> flowable =
        Flowable.create(new FlowableOnSubscribe<String>() {
          @Override
          public void subscribe(FlowableEmitter<String> emitter)
              throws Exception {
            
            String[] datas = { "Hello, World!", "こんにちは、世界！" };
            
            for (String data : datas) {
              // 購読解除されている場合は処理をやめる
              if (emitter.isCancelled()) {
                return;
              }
              // データを通知する
              emitter.onNext(data);
            }
            
            // 完了したことを通知する
            emitter.onComplete();
          }
        }, BackpressureStrategy.BUFFER);// 超過したデータはバッファする
    
    flowable
        // Subscriberの処理を別スレッドで行うようにする
        .observeOn(Schedulers.computation())
        // 購読する
        .subscribe(new Subscriber<String>() {
          // データ数のリクエストおよび購読の解除を行うオブジェクト
          private Subscription subscription;
          
          // 購読が開始された際の処理
          @Override
          public void onSubscribe(Subscription subscription) {
            // SubscriptionをSubscriber内で保持する
            this.subscription = subscription;
            // 受け取るデータ数をリクエストする
            this.subscription.request(1L);
          }
          
          // データを受け取った際の処理
          @Override
          public void onNext(String data) {
            // 実行しているスレッド名の取得
            String threadName = Thread.currentThread().getName();
            // 受け取ったデータを出力する
            System.out.println(threadName + ": " + data);
            // 次に受け取るデータ数をリクエストする
            this.subscription.request(1L);
          }
          
          // 完了を通知された際の処理
          @Override
          public void onComplete() {
            // 実行しているスレッド名の取得
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + ": 完了しました");
          }
          
          // エラーを通知された際の処理
          @Override
          public void onError(Throwable error) {
            error.printStackTrace();
          }
        });
    
    // しばらく待つ
    Thread.sleep(500L);
  }
}
