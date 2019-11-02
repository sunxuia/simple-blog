package net.sunxu.demo.sb.service;

import net.sunxu.demo.sb.bo.*;
import net.sunxu.demo.sb.entity.FastDFSFile;
import net.sunxu.demo.sb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserManageServiceImpl implements UserManageService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserBO getUser(Long userId) {
        return null;
    }

    @Override
    public Page<UserBO> rankUser(UserRankType type, Pageable pageable) {
        return null;
    }

    @Override
    public UserDetailBO getUserDetail(Long userId) {
        return null;
    }

    @Override
    public Page<UserBO> listUsers(Pageable option) {
        return null;
    }

    @Override
    public void updateUser(UserEditBO user) {

    }

    @Override
    public void updateUserAvatar(UserBO user, FastDFSFile fastDFSFile) {

    }

    @Override
    public void changeUserName(UserBO user, String newUserName) {

    }

    @Override
    public boolean isUserNameExist(String userName) {
        return false;
    }

    @Override
    public void lockUser(UserBO user) {

    }

    @Override
    public void unlockUser(UserBO user) {

    }

    @Override
    public void deleteUser(UserBO user) {

    }

    @Override
    public Page<UserEventBO> listUserEvents(Long userId, Pageable option) {
        return null;
    }

    @Override
    public List<ViewHistoryBO> getViewHistory(Long userId, Date startTime, Date endTime) {
        return null;
    }
}
