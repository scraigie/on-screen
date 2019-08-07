FROM nginx:1.17.2

ADD ./config/nginx.conf /etc/nginx/nginx.conf
# COPY ./config/default.conf.template /etc/nginx/conf.d/default.conf.template

RUN apt-get update
RUN mkdir -p /tmp/nginx/cache
# COPY docker-entrypoint.sh /
# ENTRYPOINT [ "sh", "/docker-entrypoint.sh" ]

# CMD ["nginx", "-g", "daemon off;"]