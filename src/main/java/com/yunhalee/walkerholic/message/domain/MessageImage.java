package com.yunhalee.walkerholic.message.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "message_image")
@Getter
@Setter
public class MessageImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_image_id")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "file_path")
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;
}
