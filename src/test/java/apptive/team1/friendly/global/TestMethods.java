package apptive.team1.friendly.global;

import apptive.team1.friendly.domain.user.data.constant.LanguageLevel;
import apptive.team1.friendly.domain.user.data.entity.Account;
import apptive.team1.friendly.domain.user.data.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TestMethods {
    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(String email, String firstName, String lastName) {
        List<String> languages = new ArrayList<>();
        List<LanguageLevel> languageLevels = new ArrayList<>();
        languages.add("korean");
        languageLevels.add(LanguageLevel.NATIVE);
        Account account = Account.builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .languages(languages)
                .languageLevels(languageLevels)
                .build();

        accountRepository.save(account);
        return account;
    }

    public List<MultipartFile> createImageFiles() throws IOException {
        List<MultipartFile> files = new ArrayList<>();
        MockMultipartFile file
                = new MockMultipartFile(
                "bus",
                "bus.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/bus.jpg"))
        );
        MockMultipartFile file2
                = new MockMultipartFile(
                "zidane",
                "zidane.jpeg",
                MediaType.IMAGE_JPEG_VALUE,
                new FileInputStream(new File("src/test/resources/zidane.jpg"))
        );
        files.add(file);
        files.add(file2);
        return files;
    }
}
