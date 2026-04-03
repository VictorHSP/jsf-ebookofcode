#!/bin/bash

# Nome do bucket
BUCKET_NAME=ebook-of-code
ENDPOINT="http://localhost:4566"

# Cria o bucket
aws --endpoint-url=$ENDPOINT s3 mb s3://$BUCKET_NAME

# Cria "pastas"
aws --endpoint-url=$ENDPOINT s3api put-object --bucket $BUCKET_NAME --key "author-photo/"

# Faz upload de um arquivo teste na pasta imagens
echo "teste imagem" > teste.txt
aws --endpoint-url=$ENDPOINT s3 cp teste.txt s3://$BUCKET_NAME/author-photo/

# Lista conteúdo do bucket
aws --endpoint-url=$ENDPOINT s3 ls s3://$BUCKET_NAME --recursive

# Limpa arquivo local
rm teste.txt
