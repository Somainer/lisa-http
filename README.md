# Lisa-HTTP

A tiny web framework written in [lisa-language](https://github.com/Somainer/lisa-lang).
Though this repo is marked as `Scheme`, this repo is **not** written in `Scheme`.

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

## Awesome! How can I use it in production?

Still, you won't. :(
