package com.shrona.line_demo.user.application;

import com.shrona.line_demo.line.domain.Channel;
import com.shrona.line_demo.user.domain.Group;
import com.shrona.line_demo.user.domain.User;
import com.shrona.line_demo.user.domain.UserGroup;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {

    /**
     * 그룹 생성
     */
    public Group createGroup(
        Channel channel, String name, String description, List<String> phoneList);

    /**
     * Group 정보를 단일로 조회 user와 함께 fetch join을 할지 withUser 파라미터로 설정
     */
    public Group findGroupById(Long id, Boolean withUser);

    /**
     * 그룹 아이디에 해당하는 그룹 목록 조회
     */
    public List<Group> findGroupByIdList(List<Long> id);

    /**
     * 그룹에 해당하는 유저 그룹 목록 조회(페이징 정보 추가)
     */
    public Page<UserGroup> findUserGroupByGroupId(Group group, Pageable pageable);

    /**
     * 그룹 아이디에 해당하는 라인 아이디 목록 조회
     */
    public List<String> findLineIdsByGroupIds(List<Long> groupList);

    /**
     * 그룹아이디가 존재하지 않는 그룹 목록 조회
     */
    public List<Group> findGroupListNotIn(Channel channel, List<Long> ids);

    /**
     * 그룹 목록 조회
     */
    public Page<Group> findGroupList(Channel channel, Pageable pageable);

    /**
     * 그룹 별로 라인 유저 숫자 매핑
     */
    public Map<Long, Integer> findGroupLineUserCount(List<Long> groupIds);

    /**
     * 그룹 별로 전체 유저 숫자 매핑
     */
    public Map<Long, Integer> findGroupAllUserCount(List<Long> groupIds);

    /**
     * 그룹에 전화번호 추가
     */
    public void addUserToGroup(Long groupId, List<String> phoneNumberList);

    /**
     * 그룹 목록(영구) 삭제
     */
    public void deleteGroup(List<Long> groupIdList);

    /**
     * 그룹 목록 soft delete 삭제
     */
    public void softDeleteGroup(List<Long> groupIdList);

    /**
     * 그룹 정보 업데이트(이름, 설명만)
     */
    public Group updateGroupInfo(Long groupId, String newName, String newDescription);

    /**
     * 그룹에서 휴대 전화를 기준으로 유저 목록 삭제
     */
    public void deleteUserFromGroupByPhones(Long id, List<String> phoneNumberList);

    /**
     * 그룹에서 데이터베이스 아이디를 기준으로 유저 목록 삭제
     */
    public void deleteUserFromGroupByIds(Long id, List<Long> ids);


    /**
     * 기존의 User의 휴대전화가 변경되면 UserGroup을 Before에서 After로 변경해준다.
     */
    public void mergeUserGroupBeforeToAfter(User source, User target);
}
