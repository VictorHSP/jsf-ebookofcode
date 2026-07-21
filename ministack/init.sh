#!/bin/bash

# Nome do bucket
BUCKET_NAME=ebook-of-code
ENDPOINT=${AWS_ENDPOINT_URL:-"http://localhost:4566"}

# Cria o bucket se não existir
if aws --endpoint-url=$ENDPOINT s3api head-bucket --bucket $BUCKET_NAME 2>/dev/null; then
    echo "Bucket $BUCKET_NAME already exists."
else
    echo "Creating bucket $BUCKET_NAME..."
    aws --endpoint-url=$ENDPOINT s3 mb s3://$BUCKET_NAME

    # Cria "pastas"
    aws --endpoint-url=$ENDPOINT s3api put-object --bucket $BUCKET_NAME --key "author-photo/" --checksum-algorithm SHA256

    # Faz upload de um arquivo teste na pasta imagens
    echo "teste imagem" > teste.txt
    aws --endpoint-url=$ENDPOINT s3 cp teste.txt s3://$BUCKET_NAME/author-photo/ --checksum-algorithm SHA256

    # Limpa arquivo local
    rm teste.txt
fi

# Lista conteúdo do bucket
aws --endpoint-url=$ENDPOINT s3 ls s3://$BUCKET_NAME --recursive
