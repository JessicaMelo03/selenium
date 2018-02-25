package testes;

import org.easetech.easytest.annotation.DataLoader;
import org.easetech.easytest.annotation.Param;
import org.easetech.easytest.runner.DataDrivenTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import suporte.Generator;
import suporte.Screenshot;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(DataDrivenTestRunner.class)
@DataLoader(filePaths = "InformacoesUsuarioTest.csv")
public class InformacoesUsuarioTest {
    private WebDriver navegador;

    // para pegar o nome do metodo e assim buscar pro screenshot
    @Rule
    public TestName test = new TestName();

    @Before
    public void setUp() {
        // Abrindo navegador
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\princ\\drivers\\chromedriver.exe");
        navegador = new ChromeDriver();
        navegador.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);


        //Navegando para a pagina Taskit
        navegador.manage().window().maximize();
        navegador.get("http://www.juliodelima.com.br/taskit/");

        // CLicar no SingIn na página inicial
        WebElement linksingin = navegador.findElement(By.linkText("Sign in"));
        linksingin.click();

        // Identificando o formulário de login id = singinbox
        WebElement formularioSingInBox = navegador.findElement(By.id("signinbox"));

        // Digitar no campo do name login o texto "julio0001"
        formularioSingInBox.findElement(By.name("login")).sendKeys("julio0001");

        // Digitar no campo password o texto "123456"
        formularioSingInBox.findElement(By.name("password")).sendKeys("123456");

        // Clicar no link "singin" com o texto "Sing in"

        navegador.findElement(By.linkText("SIGN IN")).click();

        // Clicar em um link que possui a class "me"
        navegador.findElement(By.className("me")).click();

        // Clicar em um link que possui o texto"MORA DATA ABOUT YOU"
        navegador.findElement(By.linkText("MORE DATA ABOUT YOU")).click();
    }


    @Test
    public void testAdicionarUmaInformacaoAdicionalDoUsuario(
            @Param(name = "tipo")String tipo,
            @Param(name = "contato")String contato,
            @Param(name = "mensagem")String mensagemEsperada) {

        // Clicar em um botão do seu xpath
        navegador.findElement(By.xpath("//button[@data-target=\"addmoredata\"]")).click();

        // Identificar a pop up onde está o formulário de id = addmoredata
        WebElement popupMoreData = navegador.findElement(By.id("addmoredata"));

        // Na combo de name = type escolher a opção "Phone"
        // 1- identifica o elemento e adiciona o elemento
        //2 - new select da classe do selenium dentro coloca o campo do elemento e o que vai selecionar
        WebElement campoType = popupMoreData.findElement(By.name("type"));
        new Select(campoType).selectByVisibleText(tipo);

        // No campo de name contact  digitar +5511999999999
        popupMoreData.findElement(By.name("contact")).sendKeys(contato);

        // Clicar no link de salvar com o texto "save" que está no pop up
        popupMoreData.findElement(By.linkText("SAVE")).click();

        // Na mensagem de id = "toast-container" validar o texto "Your contact has been added!"
        WebElement mensagempop = navegador.findElement(By.id("toast-container"));
        String mensagem = mensagempop.getText();
        assertEquals(mensagemEsperada, mensagem);


        // Validar se foi realizado o login, verificando se apareceu através da class "me" possui o texto "Hi, Julio"
//        WebElement me = navegador.findElement(By.className("me"));
        //   String textoNoElementoMe = me.getText();
        // assertEquals("Hi, Julio", textoNoElementoMe);

    }

    @Test
    public void removerUmContatoDeUmUsuario(
            @Param(name = "telefone")String telefone,
            @Param(name = "mensagem") String mensagemEsperada) {
        // Excluir o numero +5511999999998 pelo xpath //span[text()='+5511999999998']/following-sibling::a
        navegador.findElement(By.xpath("//span[text()=\"" + telefone + "\"]/following-sibling::a")).click();

        //Confirmar a janela javascrip
        navegador.switchTo().alert().accept();

        // Validar mensagem Rest in peace, dear phone!
        WebElement mensagemPop = navegador.findElement(By.id("toast-container"));
        String mensagem = mensagemPop.getText();
        assertEquals(mensagemEsperada, mensagem);

        String screenshotArquivo = "C:\\Users\\princ\\Downloads\\Estudos\\selenium com java\\fotos\\"
                + Generator.dataHoraParaArquivo() + test.getMethodName() + ".png";
        Screenshot.tirar(navegador, screenshotArquivo);

        // Aguardar até 10s para janela desapareça
        WebDriverWait aguardar = new WebDriverWait(navegador, 10);
        aguardar.until(ExpectedConditions.stalenessOf(mensagemPop));

        //Fazer logout clicar no link com o texto "Logout"
        navegador.findElement(By.linkText("Logout")).click();
    }

    @After
    public void tearDown() {
        // fechar navegador
        navegador.quit();
    }
}
