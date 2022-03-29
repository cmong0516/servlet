package hello.servlet.web.frontcontroller.v5;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import hello.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    private MyHandlerAdapter getHandlerAdapter(Object handler) {

        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("can't find handler adapter. handler = " + handler);
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object handler = getHandler(request);
        //getHandler 메서드에 request 값을 넣는다.
        //받은 request 에서 getRequestURI를 실행.
        //private final Map<String, Object> handlerMappingMap = new HashMap<>();
        //위의 handlerMappingMap 에서 requestURI를 키로하는 밸류값을 반환
        //그값을 handler 에 저장한다.
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        //handler에 저장할 값을 찾지 못했을경우 (requestURI의 정보가 handlerMappingMap에 없을때)
        //404 반환.

        MyHandlerAdapter adapter = getHandlerAdapter(handler);
        //FrontControllerServletV5 객체가 생성될때
        //initHandlerMappingMap();
        //initHandlerAdapters();     두개의 메서드가 실행된다.
        //initHandlerMappingMap()은 handlerMappingMap에 requestURI,V3의 컨트롤객체을 넣는다
        //initHandlerAdapters()는 handlerAdapters List에 ControllerV3HandlerAdapter객체를 생성한다.
        //handler에 초기화된 값이 있을경우 handler 타입의 Object를 받는 getHandlerAdapter 메서드를 실행
        //private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();
        //handlerAdapters를 돌면서 adapter를 찾는다.        현재는 V3의 핸ㄹ들러만 있다.
        //V3 핸들러를 adapter에 리턴받는다.
        //위의 adapter 는 V3핸들러를 담고있다.


        ModelView mv = adapter.handle(request, response, handler);
        //V3핸들러.handle(request,response,handler);

//        ControllerV3 controller = (ControllerV3) handler;
//        Object 타입으로 받았던 handler 를 ControllerV3 타입으로 캐스팅
//        Map<String, String> paramMap = createParamMap(request);
//        request 의 모든 parameter , values 를 paramMap 에 담아
//        ModelView mv = controller.process(paramMap);
//        v3/controller 하위 클래스중 requestURI가 일치하는것 .prcess(paramMap)
//        return mv;

        String viewName = mv.getViewName();
        //viewName을 받아 생성된 ModelView 타입의 mv 에서 ViewName을 찾는다
        MyView view = viewResolver(viewName);
        //viewResolver 메서드에 viewName 을 담아 최종 보여줄 파일의 경로를 작성.
        view.render(mv.getModel(), request, response);
    }
}