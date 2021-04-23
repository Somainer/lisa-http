# Lisa-HTTP

A tiny web framework written in [lisa-language](https://github.com/Somainer/lisa-lang).
Though this repo is marked as `Scheme`, this repo is **not** written in `Scheme`.

## A Tiny Example

```clojure
(import! lisa-http)
(lisa-http/run
  :port 5050
  :route
    (make-routes!
      ("/" "Hello, world!")
      ($"/hello/$name" $"Hello, $name!")
      ("/json" (record 'number 1 'array '(1 2 3)))))
```

Well, that's it, and you have created and run a tiny web server on local port `5050`.

There are several configurations available:

* :port port Set the listening port.
* :route route Pass a route, and you can pass multiple routes by separating them with `:route`.
* :threaded thread Set the threading configuration, `thread` could be:
  * `true` Enable threading.
  * `false` Disable threading.
  * `<number>` Execute requests in a fixed thread pool with `<number>` thread(s).
* :static directory route-prefix Host static files in `directory` with a `route-prefix`.

If you want to deep dive what exactly happened, see the simple example below.

## A Simple Example
```scheme
; Save this as app.lisa
(import! lisa-http)

(->> (make-server)
  (bind-port 5050)
  (set-router
    (make-router 
      (make-routes!
        ("/" "Hello, world!")
        ($"/hello/$name" $"Hello, $name!")
        ("/json" (record 'number 1 'array '(1 2 3)))))) ; Returns {"number": 1, "array": [1, 2, 3]}
  (start-server))
```

### What is `->>`
`->>` is a macro which takes the first argument, then apply it as the first argument of every application in rest arguments. The code above is equivalent to:
```scheme
(define server (make-server))
(bind-port server 5050)
(set-router server
  (make-router 
    (make-routes!
      ("/" "Hello, world!")
      ($"/hello/$name" $"Hello, $name!")
      ("/json" (record 'number 1 'array '(1 2 3)))))) ; Returns {"number": 1, "array": [1, 2, 3]}
(start-server server)
```

### What is `make-routes!`

`make-routes!` is another macro, mapping each route to a function.
So, there is no magic in route matching, it's just pattern matching, routing functions are just ordinary functions
in lisa.

`make-routes!` supports http methods, you can see examples to learn more.


### What is the dollar sign before string literals?

They are string interpolations, and when this occurs in function arguments, it becomes string matching templates,
and it can bind slots to function paremeters.

## Response Helpers

You can specify responses by these helpers:

* `(ok <result>)` Return status code 200 with `<result>`.
* `(err <code> <result>)` Return status code `<code>` with `<result>`.
* `(redirect-to <path>)` Return 301 redirection to `<path>`.
* `(redirect-to <path> <status>)` Return 301 redirection to `<path>`. `<status>` cound be:
  * `:permanent` 301 permanent redirection.
  * `:found` 302 redirection.
  * `<code>` That status `<code>`, so `<code>` must be a valid status code.
* `(send-file <path>)` Sends the file in `<path>`, this supports HTTP range request.

## Awesome! How can I use it in production?

Still, you won't. :(
