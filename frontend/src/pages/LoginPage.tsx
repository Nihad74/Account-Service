import {useState} from "react";
import { useNavigate } from "react-router-dom";
import '../App.css'
import type {SubmitEvent} from "react";

function LoginPage() {
    const [email, setEmail] = useState("")
    const [password, setPassword] = useState("")
    const [error, setError] = useState("")
    const [isSubmitting, setIsSubmitting] = useState(false)
    const navigate = useNavigate()

    async function handleSubmit(e: SubmitEvent<HTMLFormElement>){
        e.preventDefault()
        setError("")

        if(email === "" || password === ""){
            setError("Please fill in all fields.")
            return
        }

        setIsSubmitting(true)

        try{
            const response = await fetch("http://localhost:8080/api/auth/me", {
                method: "GET",
                headers: {
                    "Authorization": "Basic " + btoa(email + ":" + password),
                }
            })

            if(!response.ok){
                // login failed
                setError("Invalid email or password.")
                return
            } else {
                const result = await response.json()
                if(result.roles.includes("ROLE_ADMINISTRATOR")){
                    navigate("/admin")
                } else if(result.roles.includes("ROLE_ACCOUNTANT")){
                    navigate("/accountant")
                } else if(result.roles.includes("ROLE_USER")){
                    navigate("/employee")
                } else if(result.roles.includes("ROLE_AUDITOR")){
                    navigate("/auditor")
                }
            }

        }catch(err){
            setError("Could not reach the server. Please try again.")
        }finally {
            setIsSubmitting(false)
        }

    }

    return (
        <main className={"login-page"}>
            <div className={"login-card"}>
                <h1>LOG IN TO YOUR ACCOUNT</h1>
                <form className={"login-form"} onSubmit={handleSubmit}>
                    <div className={"input-group"}>
                        <label htmlFor={"email"}>Email</label>
                        <input type="email" placeholder={"Enter your email"} value={email} id={"email"}
                               onChange = {(e) => setEmail(e.target.value)}/>
                    </div>
                    <div className={"input-group"}>
                        <label htmlFor={"password"}>Password</label>
                        <input type="password" placeholder={"********"} value={password} id={"password"}
                               onChange = {(e) => setPassword(e.target.value)}/>
                    </div>
                    <div className={"error-container"}>
                        {error !== "" && <p className={"error-message"}>⚠️ {error}</p>}
                    </div>
                    <button className="login-button" type={"submit"} disabled={isSubmitting}>LOG IN</button>
                </form>
                <a className={"forgot-password"} href={"#"}>Forgot your Password?</a>
            </div>
        </main>
    )
}

export default LoginPage