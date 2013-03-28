## Description

This is a simple, silly app that slurps a url and emojinates any word
that matches an emoji name.  For example, to emojinate
https://github.com, just add it to the root of this app e.g.
[http://localhost:8080/https://github.com](http://localhost:8080/https://github.com/).

## Getting Started

1. Start the application: `lein run`
2. Go to [localhost:8080](http://localhost:8080/)

## Limitations

This app doesn't handle any number of local asset requests that
slurped urls will make. These fail for now and it's okay as long
as they don't prevent a page from rendering.
