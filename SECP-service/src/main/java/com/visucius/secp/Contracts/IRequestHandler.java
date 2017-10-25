package com.visucius.secp.Contracts;

public interface IRequestHandler<TRequest, TResponse> {

    TResponse handle(TRequest request);
}
