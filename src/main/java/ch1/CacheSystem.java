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
 * 메모리에 데이터가 있으면 동기적으로 가져오고 없으면 비동기로 웹에서 가져오는 캐쉬 시스템
 * Created by yyhyo on 2017-05-27.
 */
public class CacheSystem {
    private static final int SOME_KEY = 0;

    public static void main(String[] args) {
        CacheSystem cacheSystem = new CacheSystem();
        cacheSystem.getDataFromCache();
    }

    private void getDataFromCache() {
        Observable.create(s -> {
            String fromCache = getFromCache(SOME_KEY);
            if (fromCache != null) {
                // 동기 방식
                s.onNext(fromCache);
                s.onCompleted();
            } else {
                // 비동기 방식
                getDataAsync(SOME_KEY, res -> {
                    // onResponse
                    s.onNext(res);
                    s.onCompleted();
                }, ex -> {
                    // onFailure
                    s.onError(ex);
                });
            }
        }).subscribe(System.out::println);
    }

    private String getFromCache(int someKey) {
        boolean hasKey = false;

        if (hasKey) {
            return "cacheData";
        } else {
            return null;
        }
    }

    private void getDataAsync(int key, OnResponse onResponse, OnFailure onFailure) {
        String res = requestData(key);
        try {
            onResponse.onResponse(res);
        } catch (Exception ex) {
            onFailure.onFailure(ex);
        }
    }

    private String requestData(int key) {
        return "WebData";
    }

    @FunctionalInterface
    private interface OnResponse {
        void onResponse(String res);
    }

    @FunctionalInterface
    private interface OnFailure {
        void onFailure(Throwable th);
    }
}
