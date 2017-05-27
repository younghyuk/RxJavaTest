/*
 * MIT License
 *
 * Copyright (c) 2017 yyhyo
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ch1;

import rx.Observable;

/**
 * 동기 비동기 방식 데이터 처리
 * Created by yyhyo on 2017-05-27.
 */
public class SyncAsyncData {
    public static void main(String[] args) {
        SyncAsyncData syncAsyncData = new SyncAsyncData();
        //syncAsyncData.syncTest();
        syncAsyncData.syncAsyncTest();
    }

    private void syncTest() {
        // 기본적으로 RxJava는 동기 방식으로 데이터를 처리한다.
        // 메인 쓰레드에서 동기적으로 이벤트를 처리
        System.out.println("Main Thread : " + Thread.currentThread());
        Observable<Integer> o = Observable.create(s -> {
            s.onNext(1);
            s.onNext(2);
            s.onNext(3);
            s.onCompleted();
        });
        o.map(i -> {
            String data = "Number " + i + ", current : " + Thread.currentThread();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return data;
        }).subscribe(System.out::println);
        System.out.println("end");

        o.map(i -> "newMap " + i)
                .map(i -> "double Map " + i)
                .subscribe(System.out::println);
    }

    private void syncAsyncTest() {
        // 결국은 옵저버블에서 방출하는 쓰레드가 무엇인지가 중요하고 뒤에 붙은 map, filter 등은 그 쓰레드에 의존한다.
        // 주의 할 점은 옵저버블 내에서 여러 Thread를 생성하는 것은 추천하지 않는 방식이다.
        // 쓰레드 경합문제로 onComplete을 호출하기 까다롭기 때문.
        Observable.<Integer>create(s -> {
            // 비동기로 데이터 방출 가정
            new Thread(() -> {
                try {
                    Thread.sleep(500);
                    new Thread(() -> s.onNext(1)).start();
                    new Thread(() -> s.onNext(2)).start();
                    new Thread(() -> s.onNext(3)).start();
                    new Thread(() -> s.onNext(4)).start();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            // 이러면 onComplete 을 못 넣지만 일단 비동기 자체를 보기 위해서 편의상 이렇게 구현
        }).doOnNext(i -> System.out.println("doOnNext. currentThread : " + Thread.currentThread()))
                .filter(i -> i % 2 == 0)
                .map(i -> "value " + i + ", currentThread : " + Thread.currentThread())
                .subscribe(s -> System.out.println("value : " + s));
        System.out.println("값이 출력되기 전에 나온다.");
    }
}
