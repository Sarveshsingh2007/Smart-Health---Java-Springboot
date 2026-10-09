import axios from "axios";
export const api=axios.create({baseURL:import.meta.env.VITE_API_URL||"http://localhost:8080/api"});
api.interceptors.request.use(c=>{const t=localStorage.getItem("sha_token");if(t)c.headers.Authorization=`Bearer ${t}`;return c});
export function saveAuth(data){localStorage.setItem("sha_token",data.token);localStorage.setItem("sha_user",JSON.stringify(data))}
export function getUser(){try{return JSON.parse(localStorage.getItem("sha_user"))}catch{return null}}
export function logout(){localStorage.clear()}
