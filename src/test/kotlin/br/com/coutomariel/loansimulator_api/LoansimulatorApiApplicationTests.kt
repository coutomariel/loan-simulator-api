package br.com.coutomariel.loansimulator_api

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@ContextConfiguration
@ActiveProfiles("test")
class LoansimulatorApiApplicationTests {

    @Test
    fun contextLoads() {
    }
}
