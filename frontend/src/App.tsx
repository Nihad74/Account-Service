import './App.css'
import {useState, type SubmitEvent} from "react";

function App() {
  const [email, setEmail] = useState("")
  const [password, setPassword] = useState("")
  //const [error, setError] = useState("")
  //const [isSubmitting, setIsSubmitting] = useState(false)

  function handleSubmit(e: SubmitEvent<HTMLFormElement>){
      e.preventDefault()

      console.log("Email: ", email)
      console.log("Password: ", password)

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
            <button className="login-button" type={"submit"}>LOG IN</button>
            </form>
            <a className={"forgot-password"} href={"/forgot-password"}>Forgot your Password?</a>
        </div>
      </main>
  )
}

export default App
