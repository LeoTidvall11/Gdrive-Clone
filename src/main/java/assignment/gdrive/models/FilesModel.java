package assignment.gdrive.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Entity
//Table för att kunna ge egna värden till tabellen och justera den.
@Table(name = "folders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilesModel {

    @Id
    @GeneratedValue
    private UUID id;


    private String name;
    @Lob
    @Column(name = "content", columnDefinition = "BYTEA")
    private byte[] content;


   @ManyToOne
    @JoinColumn(name = "folder_id")
    private FoldersModel folder;
}
