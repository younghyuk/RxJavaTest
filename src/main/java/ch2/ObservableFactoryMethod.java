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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 기본적인 옵저버블 생성 함수들.
 * Created by yyhyo on 2017-05-28.
 */
public class ObservableFactoryMethod {
    public static void main(String[] args) {
        ObservableFactoryMethod observableFactoryMethod = new ObservableFactoryMethod();
        observableFactoryMethod.just();
        observableFactoryMethod.from();
        observableFactoryMethod.range();
        observableFactoryMethod.empty();
        observableFactoryMethod.never();
        observableFactoryMethod.error();
    }

    private void just() {
        Observable<String> just = Observable.just("value");
        Observable<String> just2 = Observable.just("value1", "value2");
        Observable<String> just3 = Observable.just("value1", "value2", "value3");
        // 10개 까지 입력 가능
    }

    private void from() {
        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("num : " + i);
        }
        // Iterable<T> 클래스들로 부터 값을 가져오는 옵저버블을 끌어온다. 보통 콜렉션 객체들에서 가져올 때 사용
        Observable<String> from = Observable.from(list);

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        // Future 클래스에서 가져올 수도 있다.
        Future<String> submitString = executorService.submit(() -> "String");
        Observable.from(submitString);

        Future<List<String>> submitList = executorService.submit(() -> list);
        Observable.from(submitList);
    }

    private void range() {
        Observable.range(0, 5);
    }

    private void empty() {
        // 방출하는 아이템이 없고 정상적으로 종료되는 옵저버블을 생성
        Observable.empty();
    }

    private void never() {
        // 방출하는 아이템이 없고 종료되지 않는 옵저버블을 생성
        Observable.never();
    }

    private void error() {
        // 에러를 생성하는 옵저버블 생성
        Observable.error(new Exception());
    }
}
