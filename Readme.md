Setup
=====

```shell
apt update
apt install certbot nginx
certbot
```

## HTTP -> HTTPS

1. Configure Nginx

2. Replace the contents of `/etc/nginx/sites-enabled/default` with the following

```
server {
    server_name _;

    location / {
        proxy_pass http://localhost:8081;
    }
}
```

3. Finally, restart Nginx

```shell
service nginx restart
```

Run
===

```shell
#! /bin/bash
nohup java -jar *.jar > log.txt 2> errors.txt < /dev/null &
```
