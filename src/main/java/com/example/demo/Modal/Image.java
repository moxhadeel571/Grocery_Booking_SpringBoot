package com.example.demo.Modal;

import lombok.*;
import org.bson.types.ObjectId;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class Image {
    private ObjectId id;

    private String FileName;

    private String contentType;

    private byte[] fileData;
}
