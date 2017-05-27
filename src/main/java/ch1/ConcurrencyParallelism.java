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
 * 단일 Observable은 동시성과 병렬성을 둘 다 허용하지 않는다. 대신 여러 비동기 Observable을 통해 이를 수행한다.
 * Created by yyhyo on 2017-05-27.
 */
public class ConcurrencyParallelism {
    public static void main(String[] args) {
        ConcurrencyParallelism concurrencyParallelism = new ConcurrencyParallelism();
        concurrencyParallelism.doBadTest();
    }

    private void doBadTest() {
        // 옵저버블 안에서 쓰레드 수행은 추천하지 않는다.
        Observable.create(s -> {
            new Thread(() -> {
                s.onNext("one");
                s.onNext("two");
            }).start();

            new Thread(() -> {
                s.onNext("three");
                s.onNext("four");
            }).start();

            // 이렇게 쓰레드를 돌리면 쓰레드 경합문제로 onComplete 을 제대로 넣을 수 없고 수작업으로 맞추더라도 좋은 방법은 아니다.
        });
    }

    private void doGoodTest() {
        // 하나의 옵저버블은 하나의 쓰레드에서 수행한다.
        // 옵저버블 들을 모아서 merge, flatMap 등으로 이를 동시에 수행하는 새로운 옵저버블을 만든다.

        Observable<String> a = Observable.create(s -> {
            new Thread(() -> {
                s.onNext("one");
                s.onNext("two");
                s.onCompleted();
            }).start();
        });

        Observable<String> b = Observable.create(s -> {
            new Thread(() -> {
                s.onNext("three");
                s.onNext("four");
                s.onCompleted();
            }).start();
        });

        // 여러 옵저버블을 모아서 이를 동시 수행하는 옵저버블을 새로 만든다.
        Observable<String> c = Observable.merge(a, b);

        // 이렇게 하면 one 다음 two 실행, three 다음 four 실행되고 a랑 b랑은 누가 먼저 실행될지 모른다.
    }
}
