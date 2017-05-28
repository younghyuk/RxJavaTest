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

/**
 * 옵저버블의 create 함수 이해.
 * Created by yyhyo on 2017-05-28.
 */
public class ObservableCreate {
    public static void main(String[] args) {
        ObservableCreate observableCreate = new ObservableCreate();
        observableCreate.doObservableCreate();
        observableCreate.doObservableCreate2();
    }

    private void doObservableCreate() {
        System.out.println("start");
        Observable.range(5, 3)
                .subscribe(System.out::println);
        System.out.println("end");
    }

    private void doObservableCreate2() {
        Observable<Integer> observable = Observable.create(s -> {
            System.out.println("create");
            s.onNext(5);
            s.onNext(6);
            s.onNext(7);
            s.onCompleted();
            System.out.println("complete");
        });
        // 옵저버블은 게으른 실행을 하기 때문에 생성한 시점에선 실행이 되지 않는다.

        System.out.println("start");
        // 구독할 때 실행 시작
        observable.subscribe(i -> System.out.println("value : " + i));
        System.out.println("end");
    }
}
