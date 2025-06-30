package com.simplified.picpay.model.mapper;

import com.simplified.picpay.model.domain.user.User;
import com.simplified.picpay.model.dto.user.UserDTO;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserDTO mapUserToUserDto(User user);
    User mapUserDtoToUser(UserDTO userDTO);
    void updateUserFromDto(UserDTO dto, @MappingTarget User user);
}
