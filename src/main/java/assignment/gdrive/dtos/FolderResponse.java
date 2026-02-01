package assignment.gdrive.dtos;



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

}



