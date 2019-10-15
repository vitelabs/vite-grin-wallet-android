// Copyright 2018 The Grin Developers
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

use crate::router::{Handler, HandlerObj, ResponseFuture};
use crate::web::response;
use futures::future::ok;
use hyper::header::{HeaderValue, AUTHORIZATION, WWW_AUTHENTICATE};
use hyper::{Body, Request, Response, StatusCode};
use ring::constant_time::verify_slices_are_equal;

lazy_static! {
	pub static ref GRIN_BASIC_REALM: HeaderValue =
		HeaderValue::from_str("Basic realm=GrinAPI").unwrap();
}

// Basic Authentication Middleware
pub struct BasicAuthMiddleware {
	api_basic_auth: String,
	basic_realm: &'static HeaderValue,
}

impl BasicAuthMiddleware {
	pub fn new(api_basic_auth: String, basic_realm: &'static HeaderValue) -> BasicAuthMiddleware {
		BasicAuthMiddleware {
			api_basic_auth,
			basic_realm,
		}
	}
}

impl Handler for BasicAuthMiddleware {
	fn call(
		&self,
		req: Request<Body>,
		mut handlers: Box<dyn Iterator<Item = HandlerObj>>,
	) -> ResponseFuture {
		let next_handler = match handlers.next() {
			Some(h) => h,
			None => return response(StatusCode::INTERNAL_SERVER_ERROR, "no handler found"),
		};
		if req.method().as_str() == "OPTIONS" {
			return next_handler.call(req, handlers);
		}
		if req.headers().contains_key(AUTHORIZATION)
			&& verify_slices_are_equal(
				req.headers()[AUTHORIZATION].as_bytes(),
				&self.api_basic_auth.as_bytes(),
			)
			.is_ok()
		{
			next_handler.call(req, handlers)
		} else {
			// Unauthorized 401
			unauthorized_response(&self.basic_realm)
		}
	}
}

fn unauthorized_response(basic_realm: &HeaderValue) -> ResponseFuture {
	let response = Response::builder()
		.status(StatusCode::UNAUTHORIZED)
		.header(WWW_AUTHENTICATE, basic_realm)
		.body(Body::empty())
		.unwrap();
	Box::new(ok(response))
}