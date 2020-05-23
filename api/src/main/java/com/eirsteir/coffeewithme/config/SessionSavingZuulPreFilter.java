package com.eirsteir.coffeewithme.config;

//@Slf4j
//@Component
//public class SessionSavingZuulPreFilter
//        extends ZuulFilter {
//
//    @Autowired
//    private SessionRepository repository;
//
//    @Override
//    public boolean shouldFilter() {
//        return true;
//    }
//
//    @Override
//    public Object run() {
//        RequestContext context = RequestContext.getCurrentContext();
//        HttpSession httpSession = context.getRequest().getSession();
//        Session session = repository.findById(httpSession.getId()); // .getSession()
//
//        context.addZuulRequestHeader("Cookie", "SESSION=" + httpSession.getId());
//        context.addZuulRequestHeader("Authorization", context.getRequest().getHeader("Authorization"));
//
//        log.info("ZuulPreFilter session proxy: {}", session.getId());
//        log.info("[x] ZuulPreFilter authorization proxy: {}", context.getRequest().getHeader("Authorization"));
//
//        return null;
//    }
//
//    @Override
//    public String filterType() {
//        return PRE_TYPE;
//    }
//
//    @Override
//    public int filterOrder() {
//        return 0;
//    }
//}