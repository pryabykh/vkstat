package com.pryabykh.vkstat.app.tasks;

import com.pryabykh.vkstat.core.db.CheckStatus;
import com.pryabykh.vkstat.core.db.Check;
import com.pryabykh.vkstat.core.db.Token;
import com.pryabykh.vkstat.core.db.User;
import com.pryabykh.vkstat.core.db.repositories.CheckRepository;
import com.pryabykh.vkstat.core.db.repositories.TokenRepository;
import com.pryabykh.vkstat.core.db.repositories.UserRepository;
import com.pryabykh.vkstat.core.tasks.CheckTask;
import com.pryabykh.vkstat.core.vk.VkService;
import com.pryabykh.vkstat.core.vk.ds.Online;
import com.pryabykh.vkstat.core.vk.ds.VkUser;
import com.pryabykh.vkstat.core.vk.exceptions.RequestToVkApiFailedException;
import org.springframework.stereotype.Component;

@Component
public class CheckTaskImpl implements CheckTask {
    private final VkService vkService;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final CheckRepository checkRepository;

    public CheckTaskImpl(VkService vkService, UserRepository userRepository, TokenRepository tokenRepository, CheckRepository checkRepository) {
        this.vkService = vkService;
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.checkRepository = checkRepository;
    }

    @Override
    public Runnable getRunnableFor(int ownerVkId, int vkUserId) {
        return () -> {
            User owner = userRepository.findByVkId(ownerVkId).orElseThrow(IllegalArgumentException::new);
            Token token = tokenRepository.findByUserId(owner.getId()).orElseThrow(IllegalArgumentException::new);
            VkUser targetUser = null;
            try {
                targetUser = vkService.getUser(Integer.toString(vkUserId), token.getToken()).orElseThrow(IllegalArgumentException::new);
            } catch (RequestToVkApiFailedException e) {
                e.printStackTrace();
            }
            Check check = new Check();
            check.setOwnerId(owner.getId());
            check.setVkId(targetUser.getId());
            check.setPayload(targetUser.getFirstName() + targetUser.getLastName());
//            CheckStatus checkStatus = targetUser.getOnline().equals(Online.YES) ? CheckStatus.Online : CheckStatus.Offline;
            check.setStatus(CheckStatus.Online);
            checkRepository.save(check);
        };
    }
}
