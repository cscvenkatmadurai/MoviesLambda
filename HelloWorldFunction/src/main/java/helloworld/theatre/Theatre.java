package helloworld.theatre;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Theatre {
    private long theatreId;
    private String theatreName;
    private String theatreLocation;


}
