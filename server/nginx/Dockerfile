FROM nginx:1.17
ADD ./config/nginx.conf /etc/nginx/nginx.conf
RUN apt-get update
# RUN apt-get install -y curl
RUN mkdir -p /tmp/nginx/cache
# EXPOSE 8080
# VOLUME ./logs /var/log/nginx