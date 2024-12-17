package moja.refrigerator.service.user;

import moja.refrigerator.aggregate.user.Follow;
import moja.refrigerator.aggregate.user.User;
import moja.refrigerator.repository.user.FollowRepository;
import moja.refrigerator.repository.user.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FollowServiceImpl implements FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowServiceImpl(FollowRepository followRepository, UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void toggleFollow(Long targetUserPk) {
        // 현재 로그인한 사용자 찾기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User follower = userRepository.findByUserId(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        // 팔로우 대상 사용자 찾기
        User following = userRepository.findByUserPk(targetUserPk)
                .orElseThrow(() -> new UsernameNotFoundException("팔로우 하려는 사용자를 찾을 수 없습니다."));

        // 자기 자신 팔로우 방지
        if (follower.getUserPk() == following.getUserPk()) {
            throw new IllegalArgumentException("자기 자신을 팔로우할 수 없습니다.");
        }

        // 이미 팔로우 중이면 언팔로우, 아니면 팔로우
        if (followRepository.existsByFollowerAndFollowing(follower, following)) {
            followRepository.deleteByFollowerAndFollowing(follower, following);
        } else {
            Follow follow = new Follow();
            follow.setFollower(follower);
            follow.setFollowing(following);
            followRepository.save(follow);
        }
    }
}
