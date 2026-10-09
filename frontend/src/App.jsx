import React, { useEffect, useState } from "react";



import {

    Routes,

    Route,

    Link,

    useNavigate,

    useParams,

    Navigate

} from "react-router-dom";



import { api, getUser, logout } from "./api";





/* =========================================================

   LAYOUT

   ========================================================= */



function Layout({ children }) {



    const nav = useNavigate();



    const u = getUser();



    return (

        <>



            <header>



                <Link to="/" className="brand">



                    ❤️ Smart Health Assistant



                </Link>





                <nav>



                    <Link to="/minor">



                        Minor Symptoms



                    </Link>





                    {u && (



                        <Link to="/appointments">



                            Appointments



                        </Link>



                    )}





                    {u?.role === "HOSPITAL" && (



                        <>



                            <Link to="/hospital">



                                Hospital Dashboard



                            </Link>





                            <Link to="/billing">



                                Billing



                            </Link>



                        </>



                    )}





                    {u?.role === "ADMIN" && (



                        <Link to="/admin">



                            Admin



                        </Link>



                    )}





                    {!u ? (



                        <>



                            <Link to="/login">



                                Login



                            </Link>





                            <Link to="/register">



                                Register



                            </Link>



                        </>



                    ) : (



                        <button



                            onClick={() => {



                                logout();



                                nav("/login");



                            }}



                        >



                            Logout



                        </button>



                    )}





                </nav>





            </header>





            <main>



                {children}



            </main>



        </>



    );



}





/* =========================================================

   PROTECTED ROUTES

   ========================================================= */



function Protected({ roles, children }) {



    const u = getUser();





    if (!u) {



        return <Navigate to="/login" />;



    }





    if (roles && !roles.includes(u.role)) {



        return <Navigate to="/" />;



    }





    return children;



}





/* =========================================================

   HOME

   ========================================================= */



function Home() {



    return (



        <section className="hero">



            <h1>



                Smart Health Assistant



            </h1>





            <p>



                Minor symptom guidance, hospital appointments and

                hospital billing in one secure application.



            </p>





            <div className="cards">



                <div>



                    <h3>



                        Minor Symptoms



                    </h3>





                    <p>



                        General guidance, food, precautions and

                        when to contact a doctor.



                    </p>





                    <Link

                        className="btn"

                        to="/minor"

                    >



                        Explore



                    </Link>



                </div>





                <div>



                    <h3>



                        Doctor Appointment



                    </h3>





                    <p>



                        Submit your symptoms to a hospital

                        and receive confirmation by email.



                    </p>





                    <Link

                        className="btn"

                        to="/appointments"

                    >



                        Request Appointment



                    </Link>



                </div>





                <div>



                    <h3>



                        Hospital Billing



                    </h3>





                    <p>



                        Authorized hospital employees can create

                        detailed bills and PDFs.



                    </p>





                    <Link

                        className="btn"

                        to="/login"

                    >



                        Hospital Login



                    </Link>



                </div>



            </div>



        </section>



    );



}





/* =========================================================

   LOGIN

   ========================================================= */



function Login() {



    const nav = useNavigate();





    const [f, setF] = useState({



        email: "",



        password: ""



    });





    const [err, setErr] = useState("");





    return (



        <div className="formbox">



            <h2>



                Login



            </h2>





            <form



                onSubmit={async (e) => {



                    e.preventDefault();



                    try {



                        const r = await api.post(

                            "/auth/login",

                            f

                        );





                        localStorage.setItem(

                            "sha_token",

                            r.data.token

                        );





                        localStorage.setItem(

                            "sha_user",

                            JSON.stringify(r.data)

                        );





                        nav(



                            r.data.role === "HOSPITAL"



                                ? "/hospital"



                                : r.data.role === "ADMIN"



                                    ? "/admin"



                                    : "/appointments"



                        );





                    } catch (e) {



                        setErr(



                            e.response?.data?.message ||



                            "Login failed"



                        );



                    }



                }}



            >



                <input



                    placeholder="Email"



                    type="email"



                    required



                    value={f.email}



                    onChange={(e) =>



                        setF({



                            ...f,



                            email: e.target.value



                        })



                    }



                />





                <input



                    placeholder="Password"



                    type="password"



                    required



                    value={f.password}



                    onChange={(e) =>



                        setF({



                            ...f,



                            password: e.target.value



                        })



                    }



                />





                <button className="btn">



                    Login



                </button>





                {err && (



                    <p className="error">



                        {err}



                    </p>



                )}





            </form>



        </div>



    );



}





/* =========================================================

   REGISTER

   ========================================================= */



function Register() {



    const nav = useNavigate();





    const [f, setF] = useState({



        name: "",



        email: "",



        password: "",



        phone: "",



        role: "PATIENT",



        organizationName: "",



        address: ""



    });





    const set = (k, v) =>



        setF({



            ...f,



            [k]: v



        });





    return (



        <div className="formbox">



            <h2>



                Create Account



            </h2>





            <form



                onSubmit={async (e) => {



                    e.preventDefault();



                    try {



                        const r = await api.post(

                            "/auth/register",

                            f

                        );





                        localStorage.setItem(

                            "sha_token",

                            r.data.token

                        );





                        localStorage.setItem(

                            "sha_user",

                            JSON.stringify(r.data)

                        );





                        nav(



                            f.role === "HOSPITAL"



                                ? "/hospital"



                                : "/appointments"



                        );





                    } catch (e) {



                        alert(



                            e.response?.data?.message ||



                            "Registration failed"



                        );



                    }



                }}



            >



                <input



                    placeholder="Full name"



                    required



                    value={f.name}



                    onChange={(e) =>



                        set("name", e.target.value)



                    }



                />





                <input



                    placeholder="Email"



                    type="email"



                    required



                    value={f.email}



                    onChange={(e) =>



                        set("email", e.target.value)



                    }



                />





                <input



                    placeholder="Phone / WhatsApp"



                    required



                    value={f.phone}



                    onChange={(e) =>



                        set("phone", e.target.value)



                    }



                />





                <input



                    placeholder="Password"



                    type="password"



                    required



                    value={f.password}



                    onChange={(e) =>



                        set("password", e.target.value)



                    }



                />





                <select



                    value={f.role}



                    onChange={(e) =>



                        set("role", e.target.value)



                    }



                >



                    <option value="PATIENT">



                        Patient



                    </option>





                    <option value="HOSPITAL">



                        Hospital Employee



                    </option>



                </select>





                {f.role === "HOSPITAL" && (



                    <>



                        <input



                            placeholder="Hospital name"



                            required



                            value={f.organizationName}



                            onChange={(e) =>



                                set(



                                    "organizationName",



                                    e.target.value



                                )



                            }



                        />





                        <input



                            placeholder="Hospital address"



                            value={f.address}



                            onChange={(e) =>



                                set(



                                    "address",



                                    e.target.value



                                )



                            }



                        />



                    </>



                )}





                <button className="btn">



                    Register



                </button>





            </form>





            <p>



                Admin accounts cannot be registered.



            </p>





        </div>



    );



}





/* =========================================================

   MINOR SYMPTOMS

   ========================================================= */



function Minor() {



    const [items, setItems] = useState([]);





    const navigate = useNavigate();





    useEffect(() => {



        api.get("/minor/symptoms")



            .then((r) => {



                setItems(r.data);



            })



            .catch((err) => {



                console.error(



                    "Failed to load symptoms:",



                    err



                );



            });





    }, []);





    return (



        <section>



            <h2>



                Minor Symptoms Guidance



            </h2>





            <p className="warning">



                This section is educational and not a diagnosis.

                Seek professional care for severe or worsening symptoms.



            </p>





            <div className="symptoms">



                {items.map((x) => (



                    <div



                        className="card"



                        key={x.id}



                        onClick={() =>



                            navigate(`/symptoms/${x.id}`)



                        }



                        style={{



                            cursor: "pointer"



                        }}



                    >



                        <h3>



                            {x.name}



                        </h3>





                        <p>



                            {x.description}



                        </p>





                    </div>



                ))}





            </div>



        </section>



    );



}





/* =========================================================

   SYMPTOM DETAILS

   ========================================================= */



function SymptomDetails() {



    const { id } = useParams();





    const navigate = useNavigate();





    const [symptom, setSymptom] = useState(null);





    const [loading, setLoading] = useState(true);





    const [error, setError] = useState("");





    useEffect(() => {



        api.get(`/minor/symptoms/${id}`)



            .then((r) => {



                setSymptom(r.data);



                setLoading(false);



            })



            .catch((err) => {



                console.error(err);



                setError(



                    "Unable to load symptom details."



                );



                setLoading(false);



            });





    }, [id]);





    if (loading) {



        return (



            <section>



                <p>



                    Loading symptom details...



                </p>



            </section>



        );



    }





    if (error) {



        return (



            <section>



                <p className="error">



                    {error}



                </p>





                <button



                    className="btn"



                    onClick={() =>



                        navigate("/minor")



                    }



                >



                    ← Back to Symptoms



                </button>





            </section>



        );



    }





    if (!symptom) {



        return (



            <section>



                <p>



                    Symptom not found.



                </p>





                <button



                    className="btn"



                    onClick={() =>



                        navigate("/minor")



                    }



                >



                    ← Back to Symptoms



                </button>





            </section>



        );



    }





    return (



        <section>



            <button



                className="btn"



                onClick={() =>



                    navigate("/minor")



                }



            >



                ← Back to Symptoms



            </button>





            <div className="detail">



                <h1>



                    {symptom.name}



                </h1>





                <h4>



                    Description



                </h4>





                <p>



                    {symptom.description}



                </p>





                <h4>



                    General Medicine Information



                </h4>





                <p>



                    {symptom.medicines}



                </p>





                <h4>



                    Food



                </h4>





                <p>



                    {symptom.food}



                </p>





                <h4>



                    Precautions



                </h4>





                <p>



                    {symptom.precautions}



                </p>





                <h4>



                    When to talk to a doctor



                </h4>





                <p>



                    {symptom.whenToDoctor}



                </p>





            </div>





        </section>



    );



}





/* =========================================================

   APPOINTMENTS

   ========================================================= */



function Appointments() {

    const u = getUser();
    const [hospitals, setHospitals] = useState([]);
    const [apps, setApps] = useState([]);
    const [symptoms, setSymptoms] = useState([]);

    const [f, setF] = useState({
        hospitalId: "",
        symptoms: "",
        medicalHistory: "",
        preferredDate: "",
        preferredTime: ""
    });

    const [age, setAge] = useState(u?.age || "");
    const [gender, setGender] = useState(u?.gender || "");
    const [notes, setNotes] = useState("");

    const set = (k, v) =>
        setF((current) => ({ ...current, [k]: v }));

    const tomorrow = () => {
        const d = new Date();
        d.setDate(d.getDate() + 1);
        return d.toISOString().split("T")[0];
    };

    const load = () =>
        api.get("/appointments/my")
            .then((r) => setApps(r.data))
            .catch((err) =>
                console.error("Failed to load appointments:", err)
            );

    useEffect(() => {
        api.get("/hospitals")
            .then((r) => setHospitals(r.data))
            .catch((err) =>
                console.error("Failed to load hospitals:", err)
            );

        api.get("/minor/symptoms")
            .then((r) => setSymptoms(r.data))
            .catch((err) =>
                console.error("Failed to load symptoms:", err)
            );

        if (u) load();
    }, []);

    const submitAppointment = async (e) => {
        e.preventDefault();

        if (!f.hospitalId) {
            alert("Please select a hospital.");
            return;
        }

        if (!f.symptoms.trim()) {
            alert("Please select a major symptom.");
            return;
        }

        const patientDetails = [
            age ? `Age: ${age}` : "",
            gender ? `Gender: ${gender}` : "",
            notes.trim() ? `Notes: ${notes.trim()}` : "",
            f.medicalHistory.trim()
                ? `Medical History / Allergies: ${f.medicalHistory.trim()}`
                : ""
        ]
            .filter(Boolean)
            .join(" | ");

        try {
            const appointmentData = {
                hospitalId: Number(f.hospitalId),
                symptoms: f.symptoms,
                medicalHistory: patientDetails,
                preferredDate: tomorrow(),
                preferredTime: f.preferredTime || null
            };

            await api.post("/appointments", appointmentData);

            alert("Next-day appointment request sent successfully.");

            setF({
                hospitalId: "",
                symptoms: "",
                medicalHistory: "",
                preferredDate: "",
                preferredTime: ""
            });
            setAge(u?.age || "");
            setGender(u?.gender || "");
            setNotes("");
            load();
        } catch (err) {
            console.error("Appointment error:", err);
            console.error("Backend response:", err.response?.data);
            alert(
                err.response?.data?.message ||
                "Failed to send appointment request."
            );
        }
    };

    return (
        <section className="health-page">

            <div className="health-form-card appointment-card">

                <h1 className="health-page-title">
                    Major Symptoms &amp; Next-Day Appointment
                </h1>

                <form onSubmit={submitAppointment}>

                    <div className="health-grid two-columns">
                        <div className="field-group">
                            <label>Name</label>
                            <input value={u?.name || ""} readOnly />
                        </div>

                        <div className="field-group">
                            <label>Email</label>
                            <input
                                value={u?.email || ""}
                                readOnly
                                className="readonly-blue"
                            />
                        </div>
                    </div>

                    <div className="health-grid three-columns">
                        <div className="field-group">
                            <label>Phone</label>
                            <input value={u?.phone || ""} readOnly />
                        </div>

                        <div className="field-group">
                            <label>Age</label>
                            <input
                                type="number"
                                min="1"
                                max="120"
                                placeholder="Age"
                                value={age}
                                onChange={(e) => setAge(e.target.value)}
                            />
                        </div>

                        <div className="field-group">
                            <label>Gender</label>
                            <select
                                value={gender}
                                onChange={(e) => setGender(e.target.value)}
                            >
                                <option value="">Select Gender</option>
                                <option value="Male">Male</option>
                                <option value="Female">Female</option>
                                <option value="Other">Other</option>
                                <option value="Prefer not to say">
                                    Prefer not to say
                                </option>
                            </select>
                        </div>
                    </div>

                    <div className="field-group">
                        <label>Major Symptom</label>
                        <select
                            required
                            value={f.symptoms}
                            onChange={(e) => set("symptoms", e.target.value)}
                        >
                            <option value="">Select Major Symptom</option>
                            {symptoms.map((s) => (
                                <option key={s.id} value={s.name}>
                                    {s.name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="field-group">
                        <label>Notes (Optional)</label>
                        <textarea
                            rows="3"
                            placeholder="Describe anything important about your condition..."
                            value={notes}
                            onChange={(e) => setNotes(e.target.value)}
                        />
                    </div>

                    <div className="field-group">
                        <label>Medical History / Allergies (Optional)</label>
                        <textarea
                            rows="2"
                            placeholder="Previous illness, allergies, medicines, etc."
                            value={f.medicalHistory}
                            onChange={(e) =>
                                set("medicalHistory", e.target.value)
                            }
                        />
                    </div>

                    <div className="field-group">
                        <label>Select Hospital</label>
                        <select
                            required
                            value={f.hospitalId}
                            onChange={(e) => set("hospitalId", e.target.value)}
                        >
                            <option value="">Select Hospital</option>
                            {hospitals.map((h) => (
                                <option value={h.id} key={h.id}>
                                    {h.organizationName || h.name}
                                    {h.phone ? ` — +91-${h.phone}` : ""}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="next-day-info">
                        <strong>Appointment Date:</strong>{" "}
                        {new Date(`${tomorrow()}T00:00:00`).toLocaleDateString(
                            "en-IN",
                            {
                                day: "2-digit",
                                month: "long",
                                year: "numeric"
                            }
                        )}
                        <span>
                            The hospital will confirm the final appointment time.
                        </span>
                    </div>

                    <button
                        type="submit"
                        className="major-appointment-btn"
                    >
                        Book Next-Day Appointment
                    </button>
                </form>
            </div>

            <div className="requests-section">
                <h2>My Appointment Requests</h2>

                {apps.length === 0 && (
                    <p>No appointment requests yet.</p>
                )}

                {apps.map((a) => (
                    <div className="request-card" key={a.id}>
                        <div>
                            <strong>{a.symptoms || "Appointment"}</strong>
                            <p>
                                {a.hospital?.organizationName ||
                                    a.hospital?.name ||
                                    "Hospital"}
                            </p>
                            <p>
                                Date: {a.confirmedDate || a.preferredDate || "Pending"}
                                {" | "}
                                Time: {a.confirmedTime || a.preferredTime || "Pending"}
                            </p>
                        </div>

                        <span
                            className={`appointment-status ${String(
                                a.status || "PENDING"
                            ).toLowerCase()}`}
                        >
                            {a.status || "PENDING"}
                        </span>
                    </div>
                ))}
            </div>
        </section>
    );
}


function Hospital() {

    const [d, setD] = useState(null);
    const [loading, setLoading] = useState(true);

    const load = async () => {

        try {

            setLoading(true);

            const r = await api.get("/hospital/dashboard");

            console.log(
                "Hospital dashboard:",
                r.data
            );

            setD(r.data);

        } catch (err) {

            console.error(
                "Hospital dashboard error:",
                err
            );

        } finally {

            setLoading(false);

        }
    };

    useEffect(() => {
        load();
    }, []);

    if (loading && !d) {
        return <p>Loading...</p>;
    }

    if (!d) {
        return (
            <section>
                <p className="error">
                    Unable to load hospital dashboard.
                </p>

                <button
                    className="btn"
                    onClick={load}
                >
                    Retry
                </button>
            </section>
        );
    }

    return (

        <section>

            <h2>
                {d.hospital?.organizationName ||
                    "Hospital"} Dashboard
            </h2>

            <div className="stats">

                <div>
                    Appointments

                    <b>
                        {d.appointmentCount}
                    </b>
                </div>

                <div>
                    Bills

                    <b>
                        {d.billCount}
                    </b>
                </div>

            </div>

            <h3>
                Appointment Requests
            </h3>

            {d.appointments?.length === 0 && (
                <p>
                    No appointment requests found.
                </p>
            )}

            {d.appointments?.map((a) => (

                <HospitalAppointment
                    key={a.id}
                    a={a}
                    reload={load}
                />

            ))}

        </section>
    );
}


/* =========================================================
   HOSPITAL APPOINTMENT
   ========================================================= */

function HospitalAppointment({ a, reload }) {

    const [f, setF] = useState({
        doctorName: a.doctorName || "",
        specialist: a.specialist || "",
        confirmedDate:
            a.confirmedDate ||
            a.preferredDate ||
            "",
        confirmedTime:
            a.confirmedTime ||
            a.preferredTime ||
            "",
        hospitalNotes:
            a.hospitalNotes || ""
    });

    const [updating, setUpdating] = useState(false);

    /*
     * Always use the status received from the backend.
     *
     * This is important because the parent dashboard reloads
     * the appointment after Accept / Reject.
     */
    const status = String(
        a.status || "PENDING"
    ).toUpperCase();

    const update = async (newStatus) => {

        if (updating) {
            return;
        }

        /*
         * Validation is required when accepting.
         */
        if (newStatus === "ACCEPTED") {

            if (!f.doctorName.trim()) {
                alert("Please enter the doctor name.");
                return;
            }

            if (!f.specialist) {
                alert("Please select a specialist.");
                return;
            }

            if (!f.confirmedDate) {
                alert("Please select the confirmed date.");
                return;
            }

            if (!f.confirmedTime) {
                alert("Please select the confirmed time.");
                return;
            }
        }

        try {

            setUpdating(true);

            const appointmentData = {
                doctorName: f.doctorName,
                specialist: f.specialist,
                confirmedDate: f.confirmedDate,
                confirmedTime: f.confirmedTime,
                hospitalNotes: f.hospitalNotes,
                status: newStatus
            };

            console.log(
                "Updating appointment:",
                a.id,
                appointmentData
            );

            const response = await api.patch(
                `/appointments/${a.id}/status`,
                appointmentData
            );

            console.log(
                "Updated appointment response:",
                response.data
            );

            /*
             * Reload the complete hospital dashboard.
             * The new appointment object now contains the updated
             * status, doctor, specialist, date, time and notes.
             */
            await reload();

            alert(
                newStatus === "ACCEPTED"
                    ? "Appointment accepted. Patient notified by email."
                    : "Appointment rejected. Patient notified by email."
            );

        } catch (err) {

            console.error(
                "Appointment update error:",
                err
            );

            console.error(
                "Backend response:",
                err.response?.data
            );

            alert(
                err.response?.data?.message ||
                "Failed to update appointment."
            );

        } finally {

            setUpdating(false);

        }
    };

    return (

        <div className="rowcard">

            {/* =================================================
                PATIENT INFORMATION
                ================================================= */}

            <b>
                {a.patient?.name || "Patient"} — {a.symptoms}
            </b>

            <p>
                {a.patient?.email || ""}
                {" | "}
                {a.patient?.phone || ""}
            </p>

            <p>
                <strong>
                    Symptoms:
                </strong>{" "}
                {a.symptoms}
            </p>

            {/* =================================================
                STATUS
                ================================================= */}

            <p>
                <strong>
                    Status:
                </strong>{" "}
                <strong>
                    {status}
                </strong>
            </p>

            {/* =================================================
                ACCEPTED INFORMATION
                ================================================= */}

            {status === "ACCEPTED" && (
                <div>
                    <p>
                        <strong>
                            Appointment Accepted
                        </strong>
                    </p>

                    <p>
                        The patient has been notified by email.
                    </p>
                </div>
            )}

            {/* =================================================
                REJECTED INFORMATION
                ================================================= */}

            {status === "REJECTED" && (
                <div>
                    <p>
                        <strong>
                            Appointment Rejected
                        </strong>
                    </p>

                    <p>
                        The patient has been notified by email.
                    </p>
                </div>
            )}

            {/* =================================================
                DOCTOR NAME
                ================================================= */}

            <label>
                Doctor Name
            </label>

            <input
                placeholder="Doctor name"
                value={f.doctorName}
                disabled={
                    status !== "PENDING" ||
                    updating
                }
                onChange={(e) =>
                    setF({
                        ...f,
                        doctorName: e.target.value
                    })
                }
            />

            {/* =================================================
                SPECIALIST
                ================================================= */}

            <label>
                Specialist
            </label>

            <select
                value={f.specialist}
                disabled={
                    status !== "PENDING" ||
                    updating
                }
                onChange={(e) =>
                    setF({
                        ...f,
                        specialist: e.target.value
                    })
                }
            >

                <option value="">
                    Select Specialist
                </option>

                {[
                    "General Physician",
                    "Cardiologist",
                    "Neurologist",
                    "Orthopedic Specialist",
                    "Dermatologist",
                    "ENT Specialist",
                    "Gastroenterologist",
                    "Gynecologist",
                    "Pediatrician",
                    "Pulmonologist",
                    "Dentist",
                    "Ophthalmologist",
                    "Psychiatrist",
                    "Urologist",
                    "Oncologist"
                ].map((specialist) => (

                    <option
                        key={specialist}
                        value={specialist}
                    >
                        {specialist}
                    </option>

                ))}

            </select>

            {/* =================================================
                CONFIRMED DATE
                ================================================= */}

            <label>
                Confirmed Date
            </label>

            <input
                type="date"
                value={f.confirmedDate}
                disabled={
                    status !== "PENDING" ||
                    updating
                }
                onChange={(e) =>
                    setF({
                        ...f,
                        confirmedDate:
                            e.target.value
                    })
                }
            />

            {/* =================================================
                CONFIRMED TIME
                ================================================= */}

            <label>
                Confirmed Time
            </label>

            <input
                type="time"
                value={f.confirmedTime}
                disabled={
                    status !== "PENDING" ||
                    updating
                }
                onChange={(e) =>
                    setF({
                        ...f,
                        confirmedTime:
                            e.target.value
                    })
                }
            />

            {/* =================================================
                HOSPITAL NOTES
                ================================================= */}

            <label>
                Hospital Notes
            </label>

            <textarea
                placeholder="Hospital notes"
                value={f.hospitalNotes}
                disabled={
                    status !== "PENDING" ||
                    updating
                }
                onChange={(e) =>
                    setF({
                        ...f,
                        hospitalNotes:
                            e.target.value
                    })
                }
            />

            {/* =================================================
                ACCEPT / REJECT BUTTONS
                ================================================= */}

            {status === "PENDING" && (
                <>

                    <button
                        type="button"
                        className="btn"
                        disabled={updating}
                        onClick={() =>
                            update("ACCEPTED")
                        }
                    >
                        {updating
                            ? "Updating..."
                            : "Accept"}
                    </button>

                    <button
                        type="button"
                        className="danger"
                        disabled={updating}
                        onClick={() =>
                            update("REJECTED")
                        }
                    >
                        {updating
                            ? "Updating..."
                            : "Reject"}
                    </button>

                </>
            )}

        </div>
    );
}


/* =========================================================
   BILLING
   ========================================================= */


function Billing() {

    const [patients, setPatients] = useState([]);
    const [apps, setApps] = useState([]);
    const [symptoms, setSymptoms] = useState([]);
    const [disease, setDisease] = useState("");
    const [meds, setMeds] = useState([]);
    const [lines, setLines] = useState([]);
    const [customMedicine, setCustomMedicine] = useState({
        name: "",
        price: ""
    });

    const [selectedServices, setSelectedServices] = useState([]);

    const [f, setF] = useState({
        patientId: "",
        appointmentId: "",
        icuCharges: 0,
        mriCharges: 0,
        doctorCharges: 0,
        roomCharges: 0,
        labCharges: 0,
        otherCharges: 0,
        tax: 0,
        notes: ""
    });

    const set = (k, v) =>
        setF((current) => ({
            ...current,
            [k]: v
        }));

    const services = [
        {
            id: "consultation",
            label: "Consultation",
            price: 500
        },
        {
            id: "blood",
            label: "Blood Test",
            price: 300
        },
        {
            id: "xray",
            label: "X-Ray",
            price: 700
        },
        {
            id: "mri",
            label: "MRI Scan",
            price: 5000
        },
        {
            id: "ecg",
            label: "ECG",
            price: 1200
        },
        {
            id: "icu",
            label: "ICU Bed (per day)",
            price: 8000
        }
    ];

    useEffect(() => {
        Promise.all([
            api.get("/hospital/dashboard"),
            api.get("/minor/symptoms")
        ])
            .then(([dashboard, symptomResponse]) => {
                const appointments = dashboard.data.appointments || [];

                setApps(appointments);
                setSymptoms(symptomResponse.data || []);

                const uniquePatients = [
                    ...new Map(
                        appointments
                            .filter((a) => a.patient)
                            .map((a) => [a.patient.id, a.patient])
                    ).values()
                ];

                setPatients(uniquePatients);
            })
            .catch((err) => {
                console.error("Failed to load billing data:", err);
            });
    }, []);

    const selectedPatient = patients.find(
        (p) => String(p.id) === String(f.patientId)
    );

    const patientAppointments = apps.filter(
        (a) => String(a.patient?.id) === String(f.patientId)
    );

    const toggleService = (service) => {
        setSelectedServices((current) => {
            const exists = current.includes(service.id);

            if (exists) {
                return current.filter((id) => id !== service.id);
            }

            return [...current, service.id];
        });
    };

    const applyServiceCharges = (serviceIds) => {
        let doctorCharges = 0;
        let labCharges = 0;
        let mriCharges = 0;
        let icuCharges = 0;
        let otherCharges = 0;

        if (serviceIds.includes("consultation")) {
            doctorCharges = 500;
        }

        if (serviceIds.includes("blood")) {
            labCharges += 300;
        }

        if (serviceIds.includes("xray")) {
            otherCharges += 700;
        }

        if (serviceIds.includes("mri")) {
            mriCharges = 5000;
        }

        if (serviceIds.includes("ecg")) {
            otherCharges += 1200;
        }

        if (serviceIds.includes("icu")) {
            icuCharges = 8000;
        }

        setF((current) => ({
            ...current,
            doctorCharges,
            labCharges,
            mriCharges,
            icuCharges,
            otherCharges
        }));
    };

    const handleServiceChange = (service) => {
        const exists = selectedServices.includes(service.id);

        const next = exists
            ? selectedServices.filter((id) => id !== service.id)
            : [...selectedServices, service.id];

        setSelectedServices(next);
        applyServiceCharges(next);
    };

    const loadMedicines = async (value) => {
        const selected = value ?? disease;
        setDisease(selected);

        if (!selected) {
            setMeds([]);
            return;
        }

        try {
            const r = await api.get(
                "/billing/medicines",
                {
                    params: {
                        disease: selected
                    }
                }
            );

            setMeds(r.data || []);
        } catch (err) {
            console.error("Failed to load medicines:", err);
            setMeds([]);
            alert("Failed to load medicines for this symptom.");
        }
    };

    const isMedicineSelected = (medicine) =>
        lines.some(
            (line) =>
                line.name === medicine.name &&
                line.customMedicine === false
        );

    const toggleMedicine = (medicine) => {
        const selected = isMedicineSelected(medicine);

        if (selected) {
            setLines((current) =>
                current.filter(
                    (line) =>
                        !(
                            line.name === medicine.name &&
                            line.customMedicine === false
                        )
                )
            );
            return;
        }

        setLines((current) => [
            ...current,
            {
                name: medicine.name,
                quantity: 1,
                unitPrice: medicine.defaultPrice || 0,
                customMedicine: false
            }
        ]);
    };

    const addCustomMedicine = () => {
        const name = customMedicine.name.trim();
        const price = Number(customMedicine.price);

        if (!name) {
            alert("Please enter the medicine name.");
            return;
        }

        if (!Number.isFinite(price) || price < 0) {
            alert("Please enter a valid medicine price.");
            return;
        }

        setLines((current) => [
            ...current,
            {
                name,
                quantity: 1,
                unitPrice: price,
                customMedicine: true
            }
        ]);

        setCustomMedicine({
            name: "",
            price: ""
        });
    };

    const updateLine = (index, key, value) => {
        setLines((current) =>
            current.map((line, i) =>
                i === index
                    ? { ...line, [key]: value }
                    : line
            )
        );
    };

    const removeLine = (index) => {
        setLines((current) =>
            current.filter((_, i) => i !== index)
        );
    };

    const serviceTotal = services
        .filter((service) => selectedServices.includes(service.id))
        .reduce((sum, service) => sum + service.price, 0);

    const medicineTotal = lines.reduce(
        (sum, line) =>
            sum +
            Number(line.quantity || 0) *
            Number(line.unitPrice || 0),
        0
    );

    const tax = Number(f.tax || 0);
    const total = serviceTotal + medicineTotal + tax;

    const createBill = async () => {
        if (!f.patientId) {
            alert("Please select a patient.");
            return;
        }

        if (selectedServices.length === 0 && lines.length === 0) {
            alert("Please select at least one service or medicine.");
            return;
        }

        try {
            await api.post(
                "/billing",
                {
                    ...f,
                    patientId: Number(f.patientId),
                    appointmentId: f.appointmentId
                        ? Number(f.appointmentId)
                        : null,
                    medicines: lines
                }
            );

            alert(
                "Bill created. Email sent; WhatsApp is sent if Twilio is configured."
            );

            setSelectedServices([]);
            setLines([]);
            setDisease("");
            setMeds([]);
            setCustomMedicine({ name: "", price: "" });
            setF((current) => ({
                ...current,
                appointmentId: "",
                icuCharges: 0,
                mriCharges: 0,
                doctorCharges: 0,
                roomCharges: 0,
                labCharges: 0,
                otherCharges: 0,
                tax: 0,
                notes: ""
            }));
        } catch (err) {
            console.error("Create bill error:", err);
            console.error("Backend response:", err.response?.data);

            alert(
                err.response?.data?.message ||
                "Failed to create bill."
            );
        }
    };

    return (
        <section className="health-page">

            <div className="health-form-card billing-card">

                <h1 className="billing-title">
                    Billing &amp; Summary
                </h1>

                <div className="billing-patient-select">
                    <label>Select Patient</label>
                    <select
                        value={f.patientId}
                        onChange={(e) => {
                            set("patientId", e.target.value);
                            set("appointmentId", "");
                        }}
                    >
                        <option value="">Select patient</option>
                        {patients.map((p) => (
                            <option key={p.id} value={p.id}>
                                {p.name} — {p.phone || "No phone"}
                            </option>
                        ))}
                    </select>
                </div>

                <div className="health-grid three-columns billing-patient-info">
                    <div className="field-group">
                        <label>Patient Name</label>
                        <input
                            value={selectedPatient?.name || ""}
                            readOnly
                            className="readonly-blue"
                        />
                    </div>

                    <div className="field-group">
                        <label>Email</label>
                        <input
                            value={selectedPatient?.email || ""}
                            readOnly
                            className="readonly-blue"
                        />
                    </div>

                    <div className="field-group">
                        <label>Phone</label>
                        <input
                            value={selectedPatient?.phone || ""}
                            readOnly
                        />
                    </div>
                </div>

                {f.patientId && (
                    <div className="field-group">
                        <label>Appointment</label>
                        <select
                            value={f.appointmentId}
                            onChange={(e) =>
                                set("appointmentId", e.target.value)
                            }
                        >
                            <option value="">
                                Select appointment (optional)
                            </option>
                            {patientAppointments.map((a) => (
                                <option key={a.id} value={a.id}>
                                    #{a.id} — {a.symptoms} — {
                                        a.confirmedDate ||
                                        a.preferredDate ||
                                        "Date pending"
                                    }
                                </option>
                            ))}
                        </select>
                    </div>
                )}

                <h2 className="billing-section-title">
                    Select Services / Tests
                </h2>

                <div className="service-grid">
                    {services.map((service) => (
                        <label
                            key={service.id}
                            className="service-checkbox"
                        >
                            <input
                                type="checkbox"
                                checked={selectedServices.includes(service.id)}
                                onChange={() =>
                                    handleServiceChange(service)
                                }
                            />
                            <span>
                                {service.label} (₹{service.price})
                            </span>
                        </label>
                    ))}
                </div>

                <h2 className="billing-section-title">
                    Select Major Symptom
                </h2>

                <div className="field-group">
                    <select
                        value={disease}
                        onChange={(e) =>
                            loadMedicines(e.target.value)
                        }
                    >
                        <option value="">
                            Select Major Symptom
                        </option>
                        {symptoms.map((s) => (
                            <option key={s.id} value={s.name}>
                                {s.name}
                            </option>
                        ))}
                    </select>
                </div>

                <h2 className="billing-section-title medicine-heading">
                    Medicines for this symptom:
                </h2>

                <div className="medicine-checkbox-list">
                    {meds.length === 0 && disease && (
                        <p>No medicines found for this symptom.</p>
                    )}

                    {meds.map((medicine) => (
                        <label
                            key={medicine.id}
                            className="medicine-checkbox"
                        >
                            <input
                                type="checkbox"
                                checked={isMedicineSelected(medicine)}
                                onChange={() =>
                                    toggleMedicine(medicine)
                                }
                            />
                            <span>
                                {medicine.name} (₹{medicine.defaultPrice})
                            </span>
                        </label>
                    ))}
                </div>

                <div className="custom-medicine-grid">
                    <div className="field-group">
                        <label>Medicine Name</label>
                        <input
                            placeholder="Medicine Name"
                            value={customMedicine.name}
                            onChange={(e) =>
                                setCustomMedicine({
                                    ...customMedicine,
                                    name: e.target.value
                                })
                            }
                        />
                    </div>

                    <div className="field-group">
                        <label>Medicine Price (₹)</label>
                        <input
                            type="number"
                            min="0"
                            placeholder="Medicine Price"
                            value={customMedicine.price}
                            onChange={(e) =>
                                setCustomMedicine({
                                    ...customMedicine,
                                    price: e.target.value
                                })
                            }
                        />
                    </div>
                </div>

                <button
                    type="button"
                    className="secondary-billing-btn"
                    onClick={addCustomMedicine}
                >
                    Add Medicine
                </button>

                {lines.length > 0 && (
                    <div className="selected-medicines">
                        {lines.map((line, index) => (
                            <div
                                className="selected-medicine-row"
                                key={`${line.name}-${index}`}
                            >
                                <div className="selected-medicine-name">
                                    ✓ {line.name}
                                    {line.customMedicine && " (Custom)"}
                                </div>

                                <input
                                    type="number"
                                    min="1"
                                    value={line.quantity}
                                    onChange={(e) =>
                                        updateLine(
                                            index,
                                            "quantity",
                                            e.target.value
                                        )
                                    }
                                    aria-label="Quantity"
                                />

                                <input
                                    type="number"
                                    min="0"
                                    value={line.unitPrice}
                                    onChange={(e) =>
                                        updateLine(
                                            index,
                                            "unitPrice",
                                            e.target.value
                                        )
                                    }
                                    aria-label="Price"
                                />

                                <button
                                    type="button"
                                    className="remove-medicine-btn"
                                    onClick={() => removeLine(index)}
                                >
                                    Remove
                                </button>
                            </div>
                        ))}
                    </div>
                )}

                <div className="billing-extra-grid">
                    <div className="field-group">
                        <label>Additional Tax (₹)</label>
                        <input
                            type="number"
                            min="0"
                            value={f.tax}
                            onChange={(e) =>
                                set("tax", e.target.value)
                            }
                        />
                    </div>

                    <div className="field-group">
                        <label>Bill Notes</label>
                        <input
                            placeholder="Optional notes"
                            value={f.notes}
                            onChange={(e) =>
                                set("notes", e.target.value)
                            }
                        />
                    </div>
                </div>

                <div className="billing-total-box">
                    <span>Total Amount (₹)</span>
                    <strong>{total.toFixed(2)}</strong>
                </div>

                <button
                    type="button"
                    className="create-bill-btn"
                    onClick={createBill}
                >
                    Create Bill &amp; Email Summary (with PDF)
                </button>
            </div>
        </section>
    );
}


function Admin() {



    const [d, setD] = useState(null);





    useEffect(() => {



        api.get("/admin/dashboard")



            .then((r) =>



                setD(r.data)



            );





    }, []);





    if (!d) {



        return <p>Loading...</p>;



    }





    return (



        <section>



            <h2>



                Admin Dashboard



            </h2>





            <div className="stats">



                <div>



                    Patients



                    <b>



                        {d.patients.length}



                    </b>



                </div>





                <div>



                    Hospitals



                    <b>



                        {d.hospitals.length}



                    </b>



                </div>





                <div>



                    Appointments



                    <b>



                        {d.appointments.length}



                    </b>



                </div>





                <div>



                    Bills



                    <b>



                        {d.bills.length}



                    </b>



                </div>



            </div>





            <div className="exports">



                <a



                    className="btn"



                    href={



                        (



                            import.meta.env.VITE_API_URL ||



                            "http://localhost:8080/api"



                        ) +



                        "/admin/export/patients"



                    }



                >



                    Patients Excel



                </a>





                <a



                    className="btn"



                    href={



                        (



                            import.meta.env.VITE_API_URL ||



                            "http://localhost:8080/api"



                        ) +



                        "/admin/export/appointments"



                    }



                >



                    Appointments Excel



                </a>





                <a



                    className="btn"



                    href={



                        (



                            import.meta.env.VITE_API_URL ||



                            "http://localhost:8080/api"



                        ) +



                        "/admin/export/bills"



                    }



                >



                    Bills Excel



                </a>





            </div>





            <h3>



                All Appointments



            </h3>





            {d.appointments.map((a) => (



                <div



                    className="rowcard"



                    key={a.id}



                >



                    {a.patient?.name}



                    {" — "}



                    {a.symptoms}



                    {" — "}



                    {a.status}





                </div>



            ))}
        </section>
    );
}

/* =========================================================

   MAIN APP

   ========================================================= */

export default function App() {
    return (
        <Layout>
            <Routes>
                <Route
                    path="/"
                    element={<Home />}
                />
                <Route
                    path="/minor"
                    element={<Minor />}
                />
                <Route
                    path="/symptoms/:id"
                    element={<SymptomDetails />}
                />
                <Route
                    path="/login"
                    element={<Login />}
                />
                <Route
                    path="/register"
                    element={<Register />}
                />
                <Route
                    path="/appointments"
                    element={
                        <Protected roles={["PATIENT"]}>
                            <Appointments />
                        </Protected>
                    }
                />
                <Route
                    path="/hospital"
                    element={
                        <Protected roles={["HOSPITAL"]}>
                            <Hospital />
                        </Protected>
                    }
                />
                <Route  
                    path="/billing"
                    element={
                        <Protected roles={["HOSPITAL"]}>
                            <Billing />
                        </Protected>
                    }
                />
                <Route
                    path="/admin"
                    element={
                        <Protected roles={["ADMIN"]}>
                            <Admin />
                        </Protected>
                    }
                />
                <Route
                    path="*"
                    element={
                        <Navigate to="/" />
                    }
                />
            </Routes>
        </Layout>
    );
}