package assignment.gdrive.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Entity för att visa att det ska sparas i en tabell
@Entity
//Table för att kunna ge egna värden till tabellen och justera den.
@Table(name = "folders")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class FoldersModel {
    //Id för att visa vad som är Primary Key
    //Generated value för använda SQL:s inbyggda generator för id
    @Id
    @GeneratedValue
    private UUID id;


    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserModel user;

    @OneToMany(mappedBy = "folder", cascade = CascadeType.ALL)
    private List<FilesModel> files = new ArrayList<>();



}
