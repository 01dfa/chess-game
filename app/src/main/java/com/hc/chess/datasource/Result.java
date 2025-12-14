package com.hc.chess.datasource;

public abstract class Result<T> {
    private Result() {}

    public abstract T getData();

    public static boolean isSuccess(Result result) { return result instanceof Result.Success; }

    public static boolean isError(Result result) { return result instanceof Result.Error; }

    public final static class Success<T> extends Result {
        private T data;

        public Success(T data) {
            this.data = data;
        }

        @Override
        public T getData() {
            return this.data;
        }
    }


    public final static class Error extends Result {
        private Exception data;

        public Error(Exception data) {
            this.data = data;
        }

        @Override
        public Exception getData() {
            return this.data;
        }

        public String getCause() {
            return this.data.getCause() != null ?
                    this.data.getCause().toString() : "null";
        }

        public String toString() {
            return String
                    .format("Error [%s] - Cause [%s]", this.getData(), this.getCause());
        }
    }
}
