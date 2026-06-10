#!/bin/bash
# Debezium PostgreSQL CDC connector kaydı
# docker-compose up -d sonrasında çalıştırın

echo "Debezium connector kaydediliyor..."

curl -X POST http://localhost:8083/connectors \
  -H "Content-Type: application/json" \
  -d '{
    "name": "outbox-connector",
    "config": {
      "connector.class": "io.debezium.connector.postgresql.PostgresConnector",
      "database.hostname": "postgres",
      "database.port": "5432",
      "database.user": "postgres",
      "database.password": "postgres",
      "database.dbname": "product_db",
      "database.server.name": "product_db",
      "table.include.list": "public.outbox_events",
      "plugin.name": "pgoutput",
      "topic.prefix": "outbox",
      "transforms": "outbox",
      "transforms.outbox.type": "io.debezium.transforms.outbox.EventRouter",
      "transforms.outbox.table.field.event.id": "id",
      "transforms.outbox.table.field.event.key": "aggregate_id",
      "transforms.outbox.table.field.event.type": "event_type",
      "transforms.outbox.table.field.event.payload": "payload",
      "transforms.outbox.route.by.field": "aggregate_type",
      "transforms.outbox.route.topic.replacement": "outbox.${routedByValue}"
    }
  }'

echo ""
echo "Connector durumu:"
curl http://localhost:8083/connectors/outbox-connector/status
