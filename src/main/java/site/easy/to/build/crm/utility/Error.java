package site.easy.to.build.crm.utility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Error {
    private String file;
    private String message;
    private int rowNum;
    private int colNum;
}
