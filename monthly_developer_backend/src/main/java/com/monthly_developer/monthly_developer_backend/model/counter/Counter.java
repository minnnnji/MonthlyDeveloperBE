package com.monthly_developer.monthly_developer_backend.model.counter;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Column;
import javax.persistence.Id;

@Document(collection = "counter")
@Builder
public class Counter {

    @Id
    private String _id;
    @Column(name = "post_counter")
    private String type;
    private int counter;

    public String get_id() {
        return _id;
    }

    public String getType() {
        return type;
    }

    public int getCounter() {
        return counter;
    }

    @Override
    public String toString() {
        return "Counter{" +
                "_id='" + _id + '\'' +
                ", type='" + type + '\'' +
                ", counter=" + counter +
                '}';
    }
}
