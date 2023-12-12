CREATE table order_event(
                            uuid uuid not null,
                            status varchar(50),
                            order_json jsonb
)
