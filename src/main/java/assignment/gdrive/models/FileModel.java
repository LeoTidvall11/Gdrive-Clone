package assignment.gdrive.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;


    private String name;
    @Column(name = "content")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.BINARY)
    private byte[] content;


   @ManyToOne
    @JoinColumn(name = "folder_id")
    private FolderModel folder;
}
