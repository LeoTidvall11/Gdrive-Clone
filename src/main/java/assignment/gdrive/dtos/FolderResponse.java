package assignment.gdrive.dtos;



import assignment.gdrive.models.FolderModel;

import java.util.List;
import java.util.UUID;

public record FolderResponse (
    String folderName,
    List<SubFolderInfo> subFolderInfoList,
    List<FileInfo> fileInfoList
)
{
    public record SubFolderInfo(
            UUID id, String name
    )
    {}
    public record FileInfo (
            UUID id, String name
    )
    {}

    public static FolderResponse from(FolderModel model){
        var subFolders = model.getSubFolders().stream()
                .map(f -> new SubFolderInfo(f.getId(), f.getName()))
                .toList();

                var files = model.getFiles().stream()
                        .map(file -> new FileInfo(file.getId(),file.getName()))
                        .toList();

                        return new FolderResponse(model.getName(),subFolders, files);
    }

}



