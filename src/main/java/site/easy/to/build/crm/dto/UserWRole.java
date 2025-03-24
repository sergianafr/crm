package site.easy.to.build.crm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import site.easy.to.build.crm.entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserWRole {
    private int id;
    private String email;
    private String username;
    private String role;

    public static UserWRole fromEntity(User user){
        return new UserWRole(user.getId(), user.getEmail(), user.getUsername(), "ROLE_MANAGER");
    }

}
