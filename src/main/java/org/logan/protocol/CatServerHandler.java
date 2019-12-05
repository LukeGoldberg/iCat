package org.logan.protocol;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderValues.CLOSE;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;

import org.apache.commons.lang3.StringUtils;
import org.logan.core.container.Engine;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpUtil;

public class CatServerHandler extends SimpleChannelInboundHandler<HttpObject> {

	private final Engine engine;
	
	public CatServerHandler(Engine engine) {
		this.engine = engine;
	}
	
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, HttpObject msg) {
    	if (msg instanceof HttpRequest) {
            HttpRequest req = (HttpRequest) msg;
            boolean keepAlive = HttpUtil.isKeepAlive(req);
            ResponseInfo responseInfo = new ResponseInfo();
            if (engine != null) {
            	String address = req.headers().get("Host").split(":")[0];
            	String uri = address + req.uri();
            	engine.parseRequest(uri, req, responseInfo);
            }
            if (StringUtils.isBlank(responseInfo.content)) {
            	responseInfo.content = "<html><h1>404 NOT FOUND</h1></html>";
            }
            FullHttpResponse response = new DefaultFullHttpResponse(req.protocolVersion(), OK,
            		                                                  Unpooled.wrappedBuffer(responseInfo.content.getBytes()));
            HttpHeaders headers = response.headers();
            responseInfo.getHeaders().forEach((k, v) -> {
            	headers.set(k, v);
            });
            headers.setInt(CONTENT_LENGTH, responseInfo.content.length());
            if (keepAlive) {
                if (!req.protocolVersion().isKeepAliveDefault()) {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                }
            } else {
                // Tell the client we're going to close the connection.
                response.headers().set(CONNECTION, CLOSE);
            }
            ChannelFuture f = ctx.write(response);
            if (!keepAlive) {
                f.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
	
}
