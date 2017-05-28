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

package ch2;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;

/**
 * 기초적인 옵저버블 구독 방법
 * Created by yyhyo on 2017-05-28.
 */
public class SimpleObservableSubscribe {
    public static void main(String[] args) {
        SimpleObservableSubscribe simpleObservableSubscribe = new SimpleObservableSubscribe();
        simpleObservableSubscribe.doBasicTest();
        simpleObservableSubscribe.doBasicTestWithObserver();
        simpleObservableSubscribe.doBasicTestWithObserverSubscription();
        simpleObservableSubscribe.doBasicTestWithSubscriber();
    }

    private void doBasicTest() {
        Observable.create(s -> {
            try {
                s.onNext("next");
                s.onCompleted();
            } catch (Exception ex) {
                s.onError(ex);
            }
        }).subscribe(
                System.out::println,                    // onNext
                Throwable::printStackTrace,             // onError
                () -> System.out.println("complete")    // onComplete
        );
    }

    private void doBasicTestWithObserver() {
        Observable<String> observable = Observable.create(s -> {
            try {
                s.onNext("next");
                s.onCompleted();
            } catch (Exception ex) {
                s.onError(ex);
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("complete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        };

        observable.subscribe(observer);
    }

    private void doBasicTestWithObserverSubscription() {
        Observable<String> observable = Observable.create(s -> {
            try {
                s.onNext("next");
                s.onCompleted();
            } catch (Exception ex) {
                s.onError(ex);
            }
        });

        Observer<String> observer = new Observer<String>() {
            @Override
            public void onCompleted() {
                System.out.println("complete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
            }
        };

        Subscription subscribe = observable.subscribe(observer);

        subscribe.unsubscribe();
    }

    private void doBasicTestWithSubscriber() {
        Observable<String> observable = Observable.create(s -> {
            try {
                s.onNext("next");
                s.onCompleted();
            } catch (Exception ex) {
                s.onError(ex);
            }
        });

        // Subscriber : Observer + unSubscribe 합쳐진 추상화 객체. 사실 더 많은 기능이 있지만 일단 이 정도만 알자.
        Subscriber<String> subscriber = new Subscriber<String>() {
            @Override
            public void onCompleted() {
                System.out.println("complete");
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String s) {
                System.out.println(s);
                if (s == null) {
                    // 내부 리스너에서 구독을 조건에 따라 해제 할 수 있다.
                    unsubscribe();
                }
            }
        };

        observable.subscribe(subscriber);
    }
}
